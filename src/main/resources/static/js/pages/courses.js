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

// Initial call to calculate the padding-right
export function init() {
  updatePadding();
}
