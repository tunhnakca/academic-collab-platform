import { toggleDropdown } from "./pages/header.js";
import { updatePadding, init } from "./pages/courses.js";

///////////////////////////
// Header
toggleDropdown();

///////////////////////////
// Courses

// When the document is fully loaded, start calculating padding
document.addEventListener("DOMContentLoaded", function (e) {
  init();

  // Call updatePadding when the window is resized
  window.addEventListener("resize", updatePadding);
});
