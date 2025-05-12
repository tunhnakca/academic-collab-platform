import { formatDateTime, updateSectionHeight } from "../common/helpers.js";

// Updating section user-list height
export function updateSectionPostsHeight() {
  updateSectionHeight("section-posts");
}

export function updateProjectDateTimeOnPosts() {
  const dateTimeClasses = ["posts__sidebar-course__information__value__date"];

  formatDateTime(dateTimeClasses, undefined, {
    year: "numeric",
    month: "long",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
    hour12: false,
  });
}
