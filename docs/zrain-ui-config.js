import { mountZrainUI } from "./zrain-ui.js?v=1.0.0";

// Java JWT playground and navigation remain independent of Zrain UI.
mountZrainUI({
  product: "Auth Starter",
  logoSrc: "./zrain-logo.webp",
  productHref: "#top"
});
