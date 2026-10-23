// Small helpers shared by components. Files in app/utils/ are auto-imported.

// 12450 -> "$12,450"
export function formatMoney(amount: number) {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
    maximumFractionDigits: 0,
  }).format(amount)
}

// Percent of goal reached, capped at 100 so progress bars don't overflow.
export function percentOf(raised: number, goal: number) {
  return goal > 0 ? Math.min(100, Math.round((raised / goal) * 100)) : 0
}
