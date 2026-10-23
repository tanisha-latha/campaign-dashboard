// Types matching the JSON the Spring Boot API sends and accepts.
// Exported so components can reuse them.
export type CampaignStatus = 'ACTIVE' | 'PAUSED' | 'ENDED'

// One campaign (backend: Campaign.java)
export interface Campaign {
  id: string
  name: string
  description: string
  goalAmount: number
  raisedAmount: number
  donorCount: number
  status: CampaignStatus
  createdAt: string
  updatedAt: string
}

// What the create/edit form sends (backend: CampaignRequest.java)
export interface CampaignInput {
  name: string
  description: string
  goalAmount: number
  status: CampaignStatus
}

// Dashboard summary numbers (backend: CampaignStats.java)
export interface CampaignStats {
  totalCampaigns: number
  activeCampaigns: number
  totalRaised: number
  totalGoal: number
  totalDonors: number
  percentOfGoal: number
}

// A "setup store": ref() is state, computed() is a getter, functions are actions.
// defineStore is auto-imported by @pinia/nuxt; 'campaigns' is the store's unique id.
export const useCampaignsStore = defineStore('campaigns', () => {
  // Base URL of the API, from runtimeConfig in nuxt.config.ts.
  const { apiBase } = useRuntimeConfig().public

  // --- State ---
  const campaigns = ref<Campaign[]>([])
  const stats = ref<CampaignStats | null>(null)
  const loading = ref(false) // true while the dashboard data is loading
  const error = ref<string | null>(null) // message if loading failed

  // --- Getters ---
  const isEmpty = computed(() => campaigns.value.length === 0)

  // --- Actions ---

  // Loads the list and the summary numbers at the same time.
  // $fetch is a plain async request; it works anywhere, including in stores.
  async function fetchDashboard() {
    loading.value = true
    error.value = null
    try {
      // Promise.all runs both requests in parallel and waits for both.
      const [list, summary] = await Promise.all([
        $fetch<Campaign[]>('/campaigns', { baseURL: apiBase }),
        $fetch<CampaignStats>('/campaigns/stats', { baseURL: apiBase }),
      ])
      campaigns.value = list
      stats.value = summary
    } catch (err) {
      error.value = err instanceof Error ? err.message : 'Could not load campaigns.'
    } finally {
      loading.value = false
    }
  }

  // Creates a campaign (POST) or edits one (PUT) when an id is given.
  // Errors are NOT caught here: the form catches them so it can show
  // the backend's validation messages next to the right fields.
  async function saveCampaign(input: CampaignInput, id?: string) {
    if (id) {
      await $fetch(`/campaigns/${id}`, { baseURL: apiBase, method: 'PUT', body: input })
    } else {
      await $fetch('/campaigns', { baseURL: apiBase, method: 'POST', body: input })
    }
    // Reload so the list and the totals both reflect the change.
    await fetchDashboard()
  }

  // Everything returned here is public on the store.
  return { campaigns, stats, loading, error, isEmpty, fetchDashboard, saveCampaign }
})
