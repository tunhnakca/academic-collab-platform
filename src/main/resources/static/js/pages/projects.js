import { showEmptyMessage } from "../common/helpers.js";
import { updatePadding } from "./courses.js";

export function showEmptyMessageProjects() {
  showEmptyMessage(".projects", "No projects yet.");
}

export function updateSectionProjectsHeight() {
  const header = document.querySelector("header");
  const sectionProjects = document.querySelector(".section-projects");

  if (!(header || sectionProjects)) return;

  const headerHeight = header.offsetHeight;
  sectionProjects.style.height = `calc(100vh - ${headerHeight}px`;
}
