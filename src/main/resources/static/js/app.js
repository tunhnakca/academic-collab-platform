// import "./components/alert-bar.js";
import showAlert from "./components/show-alert-bar.js";
import {
  showLoginInfoModal,
  handleLoginAlerts,
  removeLoginParams,
} from "./pages/login";
import { toggleDropdownHeader } from "./pages/header";
import { setupPasswordValidation } from "./pages/profile";
import { setupSemesterDateValidation } from "./pages/add-semester";
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
  removeHTMLTags,
} from "./pages/projects";
import {
  setupDateValidation,
  initializeAddProjectMarkdownEditor,
} from "./pages/add-project";

import {
  updateSectionUserListHeight,
  deleteUser,
  deleteUserButton,
} from "./pages/user-list";

///////////////////////////
// Show alert
///////////////////////////
document.addEventListener("DOMContentLoaded", () => {
  const alertData = localStorage.getItem("alertMessage");

  if (alertData) {
    try {
      const { message, status } = JSON.parse(alertData);
      showAlert(message, status === "success" ? "success" : "error");
    } catch (err) {
      console.error("Alert message parse error:", err);
    } finally {
      // Clear message to show only once
      localStorage.removeItem("alertMessage");
    }
  }
});

///////////////////////////
// Login Page
///////////////////////////
if (window.location.pathname.includes("login")) {
  document.addEventListener("DOMContentLoaded", (e) => {
    showLoginInfoModal();
    handleLoginAlerts();
    removeLoginParams();
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
// Add Semester Page
///////////////////////////

// When the document is fully loaded
document.addEventListener("DOMContentLoaded", function () {
  // Initialize date validation for start and end date inputs
  setupSemesterDateValidation();
});

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
  window.addEventListener("resize", updateSectionProjectsHeight());

  // If no projects, show empty message
  showEmptyMessageProjects();

  // Update Date Formats
  updateProjectDateTime();

  // Removing HTML Tags
  removeHTMLTags();

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

///////////////////////////
// User List Page
///////////////////////////

// When the document is fully loaded
document.addEventListener("DOMContentLoaded", function () {
  //Start calculating header height
  updateSectionUserListHeight();

  // Call updateSectionProjectsHeight when the window is resized
  window.addEventListener("resize", updateSectionUserListHeight());

  if (deleteUserButton) {
    deleteUser();
  }
});
