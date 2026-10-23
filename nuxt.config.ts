// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: '2025-07-15',
  devtools: { enabled: true },

  // Register the Pinia module. It creates the Pinia instance for us, shares
  // store state from the server render with the browser, and auto-imports
  // defineStore/storeToRefs plus every store in app/stores/.
  modules: ['@pinia/nuxt'],
})
