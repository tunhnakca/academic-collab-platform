import "./components/alert-bar.js";
import { showLoginInfoModal } from "./pages/login";
import { toggleDropdownHeader } from "./pages/header";
import { setupPasswordValidation } from "./pages/profile";
import {
  deleteCourseButton,
  updatePadding,
  showEmptyMessageCourses,
  deleteCourse,
} from "./pages/courses";
import {
  showEmptyMessageProjects,
  updateSectionProjectsHeight,
  updateProjectDateTime,
  deleteProjectButton,
  deleteProject,
} from "./pages/projects";
import {
  setupDateValidation,
  initializeAddProjectMarkdownEditor,
} from "./pages/add-project";

///////////////////////////
// Login Page
///////////////////////////
if (window.location.pathname.includes("login")) {
  document.addEventListener("DOMContentLoaded", (e) => {
    showLoginInfoModal();
  });
}

///////////////////////////
// Header
///////////////////////////
document.addEventListener("DOMContentLoaded", (e) => {
  toggleDropdownHeader();
});

///////////////////////////
// Profile Page
///////////////////////////
if (window.location.pathname.includes("profile")) {
  document.addEventListener("DOMContentLoaded", (e) => {
    setupPasswordValidation();
  });
}

///////////////////////////
// Courses Page
///////////////////////////

// When the document is fully loaded
document.addEventListener("DOMContentLoaded", function (e) {
  //Start calculating padding
  updatePadding();

  // Call updatePadding when the window is resized
  window.addEventListener("resize", updatePadding);

  // If no courses, show empty message
  showEmptyMessageCourses();

  // Deleting Course
  if (deleteCourseButton) {
    deleteCourse();
  }
});

///////////////////////////
// Projects Page
///////////////////////////

// When the document is fully loaded
document.addEventListener("DOMContentLoaded", function (e) {
  //Start calculating header height
  updateSectionProjectsHeight();

  // Call updateSectionProjectsHeight when the window is resized
  window.addEventListener("resize", updateSectionProjectsHeight);

  // If no projects, show empty message
  showEmptyMessageProjects();

  // Update Date Formats
  updateProjectDateTime();

  // Deleting Project
  if (deleteProjectButton) {
    deleteProject();
  }
});

///////////////////////////
// Add Project Page
///////////////////////////

// When the document is fully loaded
document.addEventListener("DOMContentLoaded", function () {
  // Initialize date validation for start and end date inputs
  setupDateValidation();

  // Initialize markdown editor for project description
  initializeAddProjectMarkdownEditor();
});
