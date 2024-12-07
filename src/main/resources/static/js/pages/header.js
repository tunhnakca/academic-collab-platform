import { toggleDropdown } from "../common/helpers.js";

// Toggles the visibility of the dropdown when clicked, and hides it when clicking outside.
export function toggleDropdownHeader() {
  toggleDropdown(".user-info__name", ".user-info__dropdown");
}
