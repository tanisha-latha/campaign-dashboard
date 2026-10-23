<script setup lang="ts">
import type { CampaignStats } from '~/stores/campaigns'

// The summary numbers from GET /api/campaigns/stats, passed in by the page.
defineProps<{
  stats: CampaignStats
}>()
</script>

<template>
  <!-- Overall campaign performance at a glance. -->
  <section class="stats">
    <div class="stat">
      <span class="label">Raised</span>
      <strong>{{ formatMoney(stats.totalRaised) }}</strong>
      <span class="sub">{{ stats.percentOfGoal }}% of {{ formatMoney(stats.totalGoal) }}</span>
    </div>
    <div class="stat">
      <span class="label">Active campaigns</span>
      <strong>{{ stats.activeCampaigns }}</strong>
      <span class="sub">of {{ stats.totalCampaigns }} total</span>
    </div>
    <div class="stat">
      <span class="label">Donors</span>
      <strong>{{ stats.totalDonors.toLocaleString() }}</strong>
      <span class="sub">across all campaigns</span>
    </div>
  </section>
</template>

<style scoped>
.stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
  gap: 1rem;
}

.stat {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 1rem;
}

.label,
.sub {
  color: #666;
  font-size: 0.85rem;
}

strong {
  font-size: 1.5rem;
}
</style>
