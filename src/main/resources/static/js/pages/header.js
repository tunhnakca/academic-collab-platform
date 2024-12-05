// Toggles the visibility of the dropdown when clicked, and hides it when clicking outside.
export function toggleDropdown() {
  const userInfo = document.querySelector(".user-info__name");
  const dropdown = document.querySelector(".user-info__dropdown");

  if (!userInfo || !dropdown) return;

  userInfo.addEventListener("click", function (e) {
    dropdown.classList.toggle("d-none");
  });

  document.addEventListener("click", function (e) {
    if (!userInfo.contains(e.target) && !dropdown.contains(e.target)) {
      dropdown.classList.add("d-none");
    }
  });
}
