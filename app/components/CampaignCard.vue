<script setup lang="ts">
import type { Campaign } from '~/stores/campaigns'

// Props: the campaign to show. Read-only inside this component.
const props = defineProps<{
  campaign: Campaign
}>()

// Emits: events this component sends up to its parent.
// The card doesn't edit anything itself; it just says "edit was clicked"
// and the page decides what to do.
const emit = defineEmits<{
  edit: [campaign: Campaign]
}>()

// computed() recalculates automatically when the campaign prop changes.
const progress = computed(() => percentOf(props.campaign.raisedAmount, props.campaign.goalAmount))
</script>

<template>
  <article class="card">
    <header class="top">
      <h3>{{ campaign.name }}</h3>
      <!-- :class adds e.g. "status-active" so each status gets its own colour. -->
      <span class="status" :class="`status-${campaign.status.toLowerCase()}`">
        {{ campaign.status }}
      </span>
    </header>

    <p class="description">{{ campaign.description }}</p>

    <!-- Progress bar: the inner bar's width is the percent raised. -->
    <div class="bar" role="progressbar" :aria-valuenow="progress" aria-valuemin="0" aria-valuemax="100">
      <div class="fill" :style="{ width: `${progress}%` }" />
    </div>

    <footer class="bottom">
      <span>
        {{ formatMoney(campaign.raisedAmount) }} of {{ formatMoney(campaign.goalAmount) }}
        · {{ campaign.donorCount }} donors
      </span>
      <!-- @click sends the "edit" event up with this campaign. -->
      <button type="button" @click="emit('edit', campaign)">Edit</button>
    </footer>
  </article>
</template>

<style scoped>
.card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 1rem;
  display: flex;
  flex-direction: column;
  gap: 0.6rem;
}

.top,
.bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.5rem;
}

h3 {
  margin: 0;
  font-size: 1.05rem;
}

.description {
  margin: 0;
  color: #555;
}

.bottom {
  font-size: 0.85rem;
  color: #555;
}

.status {
  font-size: 0.7rem;
  font-weight: 600;
  padding: 0.15rem 0.5rem;
  border-radius: 999px;
}

.status-active {
  background: #e3f6e8;
  color: #1b6b34;
}

.status-paused {
  background: #fff4d6;
  color: #7a5800;
}

.status-ended {
  background: #eee;
  color: #555;
}

.bar {
  height: 8px;
  background: #eee;
  border-radius: 999px;
  overflow: hidden;
}

.fill {
  height: 100%;
  background: #2e7d32;
}
</style>
