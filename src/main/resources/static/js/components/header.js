import { toggleDropdown } from "../common/helpers.js";

// Toggles the visibility of the dropdown when clicked, and hides it when clicking outside.
document.addEventListener("DOMContentLoaded", () => {
  // Dropdown for the user-info__name trigger
  toggleDropdown(".user-info__name", ".user-info__dropdown");
});
