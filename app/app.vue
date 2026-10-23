<script setup lang="ts">
import type { Campaign } from '~/stores/campaigns'

// Get the campaigns store (auto-imported from app/stores/).
const store = useCampaignsStore()

// storeToRefs keeps state reactive when we destructure it.
// Actions (fetchDashboard) are plain functions, so we call them on the store.
const { campaigns, stats, loading, error, isEmpty } = storeToRefs(store)

// Which form is open:
//   formOpen false            -> no form
//   formOpen true, editing null -> "New campaign" form
//   formOpen true, editing set  -> "Edit campaign" form for that campaign
const formOpen = ref(false)
const editing = ref<Campaign | null>(null)

function startCreate() {
  editing.value = null
  formOpen.value = true
}

// Called when a CampaignCard emits "edit".
function startEdit(campaign: Campaign) {
  editing.value = campaign
  formOpen.value = true
}

function closeForm() {
  formOpen.value = false
  editing.value = null
}

// Load the data once, during the server render. Pinia then sends the filled
// state to the browser, so the browser doesn't fetch it again.
await callOnce('dashboard', () => store.fetchDashboard())
</script>

<template>
  <div class="page">
    <NuxtRouteAnnouncer />

    <header class="header">
      <h1>Campaign Dashboard</h1>
      <div class="buttons">
        <button :disabled="loading" @click="store.fetchDashboard()">Reload</button>
        <button @click="startCreate">+ New campaign</button>
      </div>
    </header>

    <!-- Create / edit form: "one place to create and edit campaigns". -->
    <CampaignForm v-if="formOpen" :campaign="editing" @done="closeForm" />

    <!-- Only one of these shows, checked in order: loading, error, empty, data. -->
    <p v-if="loading">Loading campaigns…</p>

    <p v-else-if="error" class="error">
      Something went wrong: {{ error }}<br />
      Check that the backend (port 8080) and MongoDB are running.
    </p>

    <p v-else-if="isEmpty">No campaigns yet. Create one to get started.</p>

    <template v-else>
      <!-- Summary numbers for all campaigns. -->
      <StatsSummary v-if="stats" :stats="stats" />

      <!-- One card per campaign; @edit listens for the card's "edit" event. -->
      <div class="list">
        <CampaignCard
          v-for="campaign in campaigns"
          :key="campaign.id"
          :campaign="campaign"
          @edit="startEdit"
        />
      </div>
    </template>
  </div>
</template>

<style scoped>
.page {
  max-width: 760px;
  margin: 2rem auto;
  padding: 0 1rem;
  font-family: system-ui, sans-serif;
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 0.5rem;
}

h1 {
  margin: 0;
}

.buttons {
  display: flex;
  gap: 0.5rem;
}

.list {
  display: grid;
  gap: 1rem;
}

.error {
  color: #b00020;
}
</style>
