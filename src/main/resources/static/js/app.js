import { toggleDropdownHeader } from "./pages/header.js";
import {
  updatePadding,
  showEmptyMessageCourses,
  deleteCourse,
} from "./pages/courses.js";
import "./pages/add-course.js"; // Load the add-course module
import "./components/alert-bar.js";

///////////////////////////
// Header
document.addEventListener("DOMContentLoaded", (e) => {
  toggleDropdownHeader();
});

///////////////////////////
// Courses

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
