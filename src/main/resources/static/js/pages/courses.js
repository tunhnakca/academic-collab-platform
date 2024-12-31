import { async } from "regenerator-runtime";
import { showEmptyMessage } from "../common/helpers.js";
import { overlay } from "../common/config";
export const deleteCourseButton = document.querySelector(
  ".courses-buttons__link--delete"
);

// The updatePadding function calculates the padding-right for the .course elements (Because the course title was behind the course code)
export function updatePadding() {
  const courses = document.querySelectorAll(".course");

  courses.forEach((course) => {
    const courseCode = course.querySelector(".course__code");
    const courseTitle = course.querySelector(".course__title");

    if (courseCode && courseTitle) {
      const courseCodeWidth = courseCode.offsetWidth;
      courseTitle.style.paddingRight = `${courseCodeWidth}px`;
    }
  });
}

export function showEmptyMessageCourses() {
  showEmptyMessage(".courses", "No courses yet.");
}

// Deleting course
export function deleteCourse() {
  const coursesContainer = document.querySelector(".courses");
  let deleteMode = false;

  deleteCourseButton.addEventListener("click", function (e) {
    e.preventDefault();
    deleteMode = !deleteMode; // Toggle mode
    coursesContainer.classList.toggle("delete-mode", deleteMode);
  });

  coursesContainer.addEventListener("click", function (event) {
    if (deleteMode && event.target.closest(".course")) {
      event.preventDefault();
      const selectedCourse = event.target.closest(".course");
      const courseId = selectedCourse.dataset.courseId;

      // Show modal with confirmation
      showDeleteCourseModal(courseId);
    }
  });

  const showDeleteCourseModal = function (courseId) {
    const modal = document.createElement("div");
    modal.classList.add("modal-delete-course");
    modal.innerHTML = `
      <p>Are you sure you want to delete this course?</p>
      <div class="modal-delete-course__buttons">
      <button class="btn btn--danger" id="confirm-delete__course">Yes</button>
      <button class="btn btn--light" id="cancel-delete__course">No</button>
      </div>
    `;

    document.body.appendChild(modal);
    overlay.classList.remove("d-none");

    document
      .getElementById("confirm-delete__course")
      .addEventListener("click", function () {
        deleteCourseFromServer(courseId);
        modal.remove();
        overlay.classList.add("d-none");
      });

    document
      .getElementById("cancel-delete__course")
      .addEventListener("click", function () {
        modal.remove();
        overlay.classList.add("d-none");
      });
  };

  async function deleteCourseFromServer(courseId) {
    try {
      const response = await fetch(`/courses/delete/${courseId}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (response.ok) {
        location.reload();
        console.info("Course deleted successfully");
      } else {
        throw new Error("Problem deleting course");
      }
    } catch (error) {
      console.error(error.message);
    }
  }
}
