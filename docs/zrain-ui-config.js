import { mountZrainUI } from "./zrain-ui.js?v=brand-20261029-v2";

// Java JWT playground and navigation remain independent of Zrain UI.
mountZrainUI({
  product: "Auth Starter",
  logoSrc: "./zrain-logo.webp",
  productHref: "#top"
});
