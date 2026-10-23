// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: '2025-07-15',
  devtools: { enabled: true },

  // Register the Pinia module. It creates the Pinia instance for us, shares
  // store state from the server render with the browser, and auto-imports
  // defineStore/storeToRefs plus every store in app/stores/.
  modules: ['@pinia/nuxt'],

  // Settings the app reads with useRuntimeConfig().
  // Anything under "public" is also available in the browser.
  runtimeConfig: {
    public: {
      // Where the Spring Boot API lives (backend/ folder, port 8080).
      // Override without editing code: NUXT_PUBLIC_API_BASE=http://other-host/api
      apiBase: 'http://localhost:8080/api',
    },
  },
})
