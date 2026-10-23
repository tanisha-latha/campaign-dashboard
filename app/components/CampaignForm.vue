<script setup lang="ts">
import type { Campaign, CampaignInput, CampaignStatus } from '~/stores/campaigns'

// Props: the campaign being edited, or null when creating a new one.
const props = defineProps<{
  campaign: Campaign | null
}>()

// Emits: tell the parent when we're done (saved or cancelled).
const emit = defineEmits<{
  done: []
}>()

const store = useCampaignsStore()

const statuses: CampaignStatus[] = ['ACTIVE', 'PAUSED', 'ENDED']

// reactive() makes a whole object reactive. v-model in the template keeps
// these fields and the inputs in sync in both directions.
const form = reactive<CampaignInput>({
  name: '',
  description: '',
  goalAmount: 1000,
  status: 'ACTIVE',
})

const saving = ref(false)
const message = ref<string | null>(null) // general error message
const fieldErrors = ref<Record<string, string>>({}) // per-field messages from the backend

// watch() runs whenever props.campaign changes (e.g. user clicks Edit on
// another card). immediate: true also runs it once at the start.
// It fills the form for editing, or resets it for creating.
watch(
  () => props.campaign,
  (campaign) => {
    form.name = campaign?.name ?? ''
    form.description = campaign?.description ?? ''
    form.goalAmount = campaign?.goalAmount ?? 1000
    form.status = campaign?.status ?? 'ACTIVE'
    message.value = null
    fieldErrors.value = {}
  },
  { immediate: true },
)

async function submit() {
  saving.value = true
  message.value = null
  fieldErrors.value = {}
  try {
    // Spread ({ ...form }) sends a plain copy of the form data.
    await store.saveCampaign({ ...form }, props.campaign?.id)
    emit('done')
  } catch (err: any) {
    // $fetch errors carry the backend's JSON body in err.data.
    // GlobalExceptionHandler.java sends { message, errors: { field: text } }.
    message.value = err?.data?.message ?? 'Could not save the campaign. Is the backend running?'
    fieldErrors.value = err?.data?.errors ?? {}
  } finally {
    saving.value = false
  }
}
</script>

<template>
  <!-- @submit.prevent stops the browser reloading the page and calls submit(). -->
  <form class="form" @submit.prevent="submit">
    <h2>{{ campaign ? 'Edit campaign' : 'New campaign' }}</h2>

    <label>
      Name
      <input v-model="form.name" type="text" maxlength="100" required />
      <small v-if="fieldErrors.name" class="error">{{ fieldErrors.name }}</small>
    </label>

    <label>
      Description
      <textarea v-model="form.description" rows="3" maxlength="500" required />
      <small v-if="fieldErrors.description" class="error">{{ fieldErrors.description }}</small>
    </label>

    <div class="row">
      <label>
        Goal ($)
        <!-- .number turns the input's text into a number. -->
        <input v-model.number="form.goalAmount" type="number" min="1" step="1" required />
        <small v-if="fieldErrors.goalAmount" class="error">{{ fieldErrors.goalAmount }}</small>
      </label>

      <label>
        Status
        <select v-model="form.status">
          <option v-for="s in statuses" :key="s" :value="s">{{ s }}</option>
        </select>
      </label>
    </div>

    <p v-if="message" class="error">{{ message }}</p>

    <div class="actions">
      <button type="button" @click="emit('done')">Cancel</button>
      <button type="submit" :disabled="saving">
        {{ saving ? 'Saving…' : campaign ? 'Save changes' : 'Create campaign' }}
      </button>
    </div>
  </form>
</template>

<style scoped>
.form {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 1rem;
  background: #fafafa;
}

h2 {
  margin: 0;
  font-size: 1.1rem;
}

label {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  font-size: 0.9rem;
  flex: 1;
}

input,
textarea,
select {
  font: inherit;
  padding: 0.4rem;
}

.row {
  display: flex;
  gap: 1rem;
}

.actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
}

.error {
  color: #b00020;
  margin: 0;
}
</style>
