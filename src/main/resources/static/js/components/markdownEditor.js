// src/js/components/markdownEditor.js
import EasyMDE from "easymde";
import hljs from "highlight.js/lib/core";

// Web Development
import javascript from "highlight.js/lib/languages/javascript";
import typescript from "highlight.js/lib/languages/typescript";
import css from "highlight.js/lib/languages/css";
import scss from "highlight.js/lib/languages/scss";
import xml from "highlight.js/lib/languages/xml";
import markdown from "highlight.js/lib/languages/markdown";

// Backend Languages
import java from "highlight.js/lib/languages/java";
import python from "highlight.js/lib/languages/python";
import csharp from "highlight.js/lib/languages/csharp";
import ruby from "highlight.js/lib/languages/ruby";
import go from "highlight.js/lib/languages/go";
import rust from "highlight.js/lib/languages/rust";
import kotlin from "highlight.js/lib/languages/kotlin";
import swift from "highlight.js/lib/languages/swift";

// Database & Query Languages
import sql from "highlight.js/lib/languages/sql";
import graphql from "highlight.js/lib/languages/graphql";

// System & Low Level
import bash from "highlight.js/lib/languages/bash";
import powershell from "highlight.js/lib/languages/powershell";
import c from "highlight.js/lib/languages/c";
import cpp from "highlight.js/lib/languages/cpp";
import arduino from "highlight.js/lib/languages/arduino";

// Configuration & Data Formats
import json from "highlight.js/lib/languages/json";
import yaml from "highlight.js/lib/languages/yaml";
import ini from "highlight.js/lib/languages/ini";

// DevOps & Cloud
import dockerfile from "highlight.js/lib/languages/dockerfile";
import nginx from "highlight.js/lib/languages/nginx";
import apache from "highlight.js/lib/languages/apache";

// Register all languages
hljs.registerLanguage("javascript", javascript);
hljs.registerLanguage("typescript", typescript);
hljs.registerLanguage("css", css);
hljs.registerLanguage("scss", scss);
hljs.registerLanguage("xml", xml);
hljs.registerLanguage("html", xml); // XML işleyici HTML için de çalışır
hljs.registerLanguage("markdown", markdown);
hljs.registerLanguage("java", java);
hljs.registerLanguage("python", python);
hljs.registerLanguage("csharp", csharp);
hljs.registerLanguage("ruby", ruby);
hljs.registerLanguage("go", go);
hljs.registerLanguage("rust", rust);
hljs.registerLanguage("kotlin", kotlin);
hljs.registerLanguage("swift", swift);
hljs.registerLanguage("sql", sql);
hljs.registerLanguage("graphql", graphql);
hljs.registerLanguage("bash", bash);
hljs.registerLanguage("shell", bash); // bash işleyici shell için de çalışır
hljs.registerLanguage("powershell", powershell);
hljs.registerLanguage("c", c);
hljs.registerLanguage("cpp", cpp);
hljs.registerLanguage("arduino", arduino);
hljs.registerLanguage("json", json);
hljs.registerLanguage("yaml", yaml);
hljs.registerLanguage("ini", ini);
hljs.registerLanguage("dockerfile", dockerfile);
hljs.registerLanguage("nginx", nginx);
hljs.registerLanguage("apache", apache);

// ---------- Color code blocks ----------
export function highlightCodeBlocks(container) {
  (container || document).querySelectorAll("pre code").forEach((block) => {
    hljs.highlightElement(block);

    const languageClass = Array.from(block.classList).find((className) =>
      className.startsWith("language-")
    );
    if (languageClass) {
      block.parentElement.setAttribute(
        "data-language",
        languageClass.replace("language-", "")
      );
    } else {
      block.parentElement.removeAttribute("data-language");
    }
  });
}

export function setupMarkdownEditor(textAreaElement) {
  if (!textAreaElement) {
    console.warn(`Textarea element not found`);
    return;
  }

  const editor = new EasyMDE({
    element: textAreaElement,
    spellChecker: false,
    autofocus: false,
    toolbar: [
      "bold",
      "italic",
      "heading",
      "|",
      "code",
      "unordered-list",
      "ordered-list",
      "|",
      "link",
      "image",
      "|",
      "preview",
      "side-by-side",
      "fullscreen",
      "|",
      "undo",
      "redo",
      "|",
      "guide",
    ],
    autosave: {
      enabled: false, // Autosave can cause confusion for multiple forms; better set 'false'
    },
    previewRender: function (plainText, preview) {
      const htmlContent = this.parent.markdown(plainText);
      preview.innerHTML = htmlContent;
      highlightCodeBlocks(preview);
      return preview.innerHTML;
    },
  });

  return editor;
}
