import { async } from "regenerator-runtime";
import { showEmptyMessage, formatDateTime } from "../common/helpers.js";
import { overlay } from "../common/config";
export const deleteProjectButton = document.querySelector(
  ".project__sidebar-course-buttons__delete--project"
);

export function showEmptyMessageProjects() {
  showEmptyMessage(".projects", "No projects yet.");
}

export function updateSectionProjectsHeight() {
  const header = document.querySelector("header");
  const sectionProjects = document.querySelector(".section-projects");

  if (!header || !sectionProjects) return;

  const headerHeight = header.offsetHeight;
  sectionProjects.style.height = `calc(100vh - ${headerHeight}px`;
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
        deleteProject(projectId);
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

  async function deleteProject(projectId) {
    try {
      const response = await fetch(`/projects/delete/${projectId}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (response.ok) {
        location.reload();
        console.info("Project deleted successfuly");
      } else {
        throw new Error("Problem deleting project");
      }
    } catch (error) {
      console.error(error.message);
    }
  }
}
