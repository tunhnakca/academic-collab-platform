export function setCurrentYear() {
  const footer = document.querySelector(".footer");
  const yearEl = footer.querySelector(".year");

  if (!footer || !yearEl) return;

  const currentYear = new Date().getFullYear();
  yearEl.textContent = currentYear;
}
