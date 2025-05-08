import { async } from "regenerator-runtime";
import {
  showEmptyMessage,
  formatDateTime,
  updateSectionHeight,
} from "../common/helpers.js";
import { overlay } from "../common/config";
export const deleteProjectButton = document.querySelector(
  ".project__sidebar-course-buttons__delete--project"
);

export function showEmptyMessageProjects() {
  showEmptyMessage(".projects", "No projects yet.");
}

// Updating section projects height
export function updateSectionProjectsHeight() {
  updateSectionHeight("section-projects");
}

export function updateProjectDateTime() {
  const dateTimeClasses = [
    "project__date-range--start",
    "project__date-range--end",
  ];

  formatDateTime(dateTimeClasses, undefined, {
    year: "numeric",
    month: "long",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
    hour12: false,
  });
}

// Removing HTML Tags
export function removeHTMLTags() {
  const description = document.querySelectorAll(".project__description");
  description.forEach((desc) => {
    // First process the pre and code block
    let cleanText = desc.innerHTML
      .replace(/<pre><code>([\s\S]*?)<\/code><\/pre>/gi, (match, p1) => {
        return "\n" + p1 + "\n";
      })
      // Clean the other tags
      .replace(/<[^>]*>/g, "")
      .replace(/&nbsp;/g, " ")
      .trim();

    // Take each line to array
    let lines = cleanText.split("\n");
    // Filter and merge lines that not empty
    cleanText = lines.filter((line) => line.trim() !== "").join("\n");

    desc.textContent = cleanText;
  });
}

// Deleting project
export function deleteProject() {
  const projectsContainer = document.querySelector(".projects");
  let deleteMode = false;

  deleteProjectButton.addEventListener("click", function (e) {
    e.preventDefault();
    deleteMode = !deleteMode; // Toggle mode
    projectsContainer.classList.toggle("delete-mode", deleteMode);
  });

  projectsContainer.addEventListener("click", function (event) {
    if (deleteMode && event.target.closest(".project")) {
      event.preventDefault();
      const selectedProject = event.target.closest(".project");
      const projectId = selectedProject.dataset.projectId;

      // Show modal with confirmation
      showDeleteProjectModal(projectId);
    }
  });

  const showDeleteProjectModal = function (projectId) {
    const modal = document.createElement("div");
    modal.classList.add("modal-delete-project");
    modal.innerHTML = `
      <p>Are you sure you want to delete this project?</p>
      <div class="modal-delete-project__buttons">
      <button class="btn btn--danger" id="confirm-delete__project">Yes</button>
      <button class="btn btn--light" id="cancel-delete__project">No</button>
      </div>
    `;

    document.body.appendChild(modal);
    overlay.classList.remove("d-none");

    document
      .getElementById("confirm-delete__project")
      .addEventListener("click", function () {
        deleteProjectFromServer(projectId);
        modal.remove();
        overlay.classList.add("d-none");
      });

    document
      .getElementById("cancel-delete__project")
      .addEventListener("click", function () {
        modal.remove();
        overlay.classList.add("d-none");
      });
  };

  async function deleteProjectFromServer(projectId) {
    try {
      const response = await fetch(`/api/projects/delete/${projectId}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
        },
      });

      const data = await response.json();

      if (response.ok) {
        // Set message to localStorage
        localStorage.setItem(
          "alertMessage",
          JSON.stringify({ message: data.message, status: "success" })
        );
        // Reload the page
        location.reload();
      } else {
        showAlert(data.message || "Unexpected error", "error");
      }
    } catch (error) {
      console.error(error.message);
      showAlert("Network error or server unreachable", "error");
    }
  }
}
