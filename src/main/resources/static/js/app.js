// import "./components/alert-bar.js";
import showAlert from "./components/show-alert-bar.js";
import { highlightCodeBlocks } from "./components/markdownEditor";
import {
  showLoginInfoModal,
  handleLoginAlerts,
  removeLoginParams,
  showForgotPasswordModal,
  setupNumberValidation,
} from "./pages/login";
import { setupResetPasswordValidation } from "./pages/reset-password";
import { toggleDropdownHeader } from "./pages/header";
import { setCurrentYear } from "./pages/footer";
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

import {
  updateSectionPostsHeight,
  updateProjectDateTimeOnPosts,
  initializePostsMarkdownEditor,
  initializeReplyScrollToForm,
  initializeRepliesToggle,
  initializeReplyMarkdownEditor,
  deletePostOrReply,
} from "./pages/posts";

///////////////////////////
// Show alert
///////////////////////////
document.addEventListener("DOMContentLoaded", () => {
  const alertData = localStorage.getItem("alertMessage");

  if (alertData) {
    try {
      const { message, status } = JSON.parse(alertData);
      showAlert(message, status === "success" ? "success" : "error");
      console.info(message);
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
document.addEventListener("DOMContentLoaded", (e) => {
  if (document.querySelector(".container-login")) {
    showLoginInfoModal();
    handleLoginAlerts();
    removeLoginParams();
    showForgotPasswordModal();
    setupNumberValidation();
  }
});

///////////////////////////
// Reset Password Page
///////////////////////////
document.addEventListener("DOMContentLoaded", (e) => {
  if (document.querySelector(".container__reset-password")) {
    setupResetPasswordValidation();
  }
});

///////////////////////////
// Header
///////////////////////////
document.addEventListener("DOMContentLoaded", (e) => {
  toggleDropdownHeader();
});

///////////////////////////
// Footer
///////////////////////////
document.addEventListener("DOMContentLoaded", (e) => {
  setCurrentYear();
});

///////////////////////////
// Profile Page
///////////////////////////
document.addEventListener("DOMContentLoaded", (e) => {
  if (document.querySelector(".section-profile")) {
    setupPasswordValidation();
  }
});

///////////////////////////
// Add Semester Page
///////////////////////////

// When the document is fully loaded
document.addEventListener("DOMContentLoaded", function () {
  if (document.querySelector(".section-add-semester")) {
    // Initialize date validation for start and end date inputs
    setupSemesterDateValidation();
  }
});

///////////////////////////
// Courses Page
///////////////////////////

// When the document is fully loaded
document.addEventListener("DOMContentLoaded", function (e) {
  if (document.querySelector(".section-courses")) {
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
  }
});

///////////////////////////
// Projects Page
///////////////////////////

// When the document is fully loaded
document.addEventListener("DOMContentLoaded", function (e) {
  if (document.querySelector(".section-projects")) {
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
  }
});

///////////////////////////
// Add Project Page
///////////////////////////

// When the document is fully loaded
document.addEventListener("DOMContentLoaded", function () {
  if (document.querySelector(".section-add-project")) {
    // Initialize date validation for start and end date inputs
    setupDateValidation();

    // Initialize markdown editor for project description
    initializeAddProjectMarkdownEditor();
  }
});

///////////////////////////
// User List Page
///////////////////////////

// When the document is fully loaded
document.addEventListener("DOMContentLoaded", function () {
  if (document.querySelector(".section-user-list")) {
    //Start calculating header height
    updateSectionUserListHeight();

    // Call updateSectionProjectsHeight when the window is resized
    window.addEventListener("resize", updateSectionUserListHeight());

    if (deleteUserButton) {
      deleteUser();
    }
  }
});

///////////////////////////
// Posts Page
///////////////////////////

// When the document is fully loaded
document.addEventListener("DOMContentLoaded", function () {
  if (document.querySelector(".section-posts")) {
    //Start calculating header height
    updateSectionPostsHeight();

    // Call updateSectionProjectsHeight when the window is resized
    window.addEventListener("resize", updateSectionPostsHeight());

    // Update Date Formats
    updateProjectDateTimeOnPosts();

    // Highlight code blocks
    highlightCodeBlocks();

    // Initialize markdown editor for answer
    initializePostsMarkdownEditor();

    // Initialize markdown editor for reply
    initializeReplyMarkdownEditor();

    // Scrolling form
    initializeReplyScrollToForm();

    // Showing replies block
    initializeRepliesToggle();

    //Deleting post or reply
    deletePostOrReply();
  }
});
