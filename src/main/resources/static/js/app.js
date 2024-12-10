import { showLoginInfoModal } from "./pages/login.js";
import { toggleDropdownHeader } from "./pages/header.js";
import { setupPasswordValidation } from "./pages/profile";
import {
  updatePadding,
  showEmptyMessageCourses,
  deleteCourse,
} from "./pages/courses.js";
import "./pages/add-course.js"; // Load the add-course module
import "./components/alert-bar.js";

///////////////////////////
// Login Page
if (window.location.pathname.includes("login")) {
  document.addEventListener("DOMContentLoaded", (e) => {
    showLoginInfoModal();
  });
}

///////////////////////////
// Header
document.addEventListener("DOMContentLoaded", (e) => {
  toggleDropdownHeader();
});

///////////////////////////
// Profile Page
if (window.location.pathname.includes("profile")) {
  document.addEventListener("DOMContentLoaded", (e) => {
    setupPasswordValidation();
  });
}

///////////////////////////
// Courses Page

// When the document is fully loaded
document.addEventListener("DOMContentLoaded", function (e) {
  //Start calculating padding
  updatePadding();

  // Call updatePadding when the window is resized
  window.addEventListener("resize", updatePadding);

  // If no courses, show empty message
  showEmptyMessageCourses();

  // Deleting Course
  deleteCourse();
});
