/**
 * Pembangun / Developer: Zrain
 * Dicipta / Created: 2026-10-27 (bukti: sejarah Git zrain-auth-starter)
 * Dikemas kini / Last updated: 2026-11-04
 * Fail / File: docs/zrain-ui-config.js
 * BM: Aktifkan Golden Reference dan ScrollSpy untuk empat seksyen panduan Auth Starter.
 * EN: Enable Golden Reference and ScrollSpy for Auth Starter's four guide sections.
 */

// BM: Import UI rasmi yang telah disegerakkan kepada direktori docs.
// EN: Import the official UI synchronized into the docs directory.
import { mountZrainUI } from "./zrain-ui.js?v=a109401";

// BM: Kekalkan header/footer rasmi tanpa mengubah backend Java atau logik playground.
// EN: Preserve official branding without changing the Java backend or playground behavior.
mountZrainUI({
  // BM: Nama produk yang dipaparkan dalam header.
  // EN: Product identity shown in the header.
  product: "Auth Starter",
  // BM: Rujuk logo Zrain tempatan.
  // EN: Use the local official Zrain logo.
  logoSrc: "./zrain-logo.webp",
  // BM: Pautan nama produk membawa ke hero paling atas.
  // EN: The product name links to the top hero.
  productHref: "#top",
  // BM: Empat pautan ini ialah item menu asal, bukan fungsi baharu.
  // EN: These are the original four navigation links, not new features.
  navigation: [
    { label: "Features", href: "#features", current: true },
    { label: "Architecture", href: "#architecture" },
    { label: "Playground", href: "#playground" },
    { label: "Quickstart", href: "#quickstart" }
  ],
  // BM: Klik dan scroll sama-sama menentukan seksyen aktif tanpa delay buatan.
  // EN: Both clicking and scrolling track the active section without artificial delay.
  scrollSpy: true
});
