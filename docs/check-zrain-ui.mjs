/**
 * Pembangun / Developer: Zrain
 * Dicipta / Created: 2026-10-27 (bukti: sejarah Git zrain-auth-starter)
 * Dikemas kini / Last updated: 2026-11-04
 * Fail / File: docs/check-zrain-ui.mjs
 * BM: Pengujian static UI asal diteruskan dengan pemeriksaan ScrollSpy dan cache key.
 * EN: Preserve original static UI checks and add ScrollSpy and cache-key assertions.
 */
import {readFile} from "node:fs/promises";
import assert from "node:assert/strict";

const read=path=>readFile(new URL(path,import.meta.url),"utf8");
const html=await read("./index.html");
const ui=await read("./zrain-ui.js");
const css=await read("./brand.css");
assert.match(html,/class="zrain-brand__home" href="https:\/\/zrain\.web\.app"/);
assert.match(html,/class="zrain-footer__home" href="https:\/\/zrain\.web\.app"/);
assert.match(html,/Made for people\. Built with purpose\./);
assert.doesNotMatch(html,/class="zrain-brand__tagline"/);
assert.match(html,/zrain-ui-config\.js/);
assert.match(html,/id="playground"/);
assert.match(html,/href="#quickstart"/);
assert.match(ui,/export function mountZrainUI/);
assert.match(ui,/https:\/\/zrain\.web\.app/);
assert.match(css,/\.zrain-footer \.zrain-footer__home/);

// ZRAIN-UI-AUTH-STARTER-TESTS-20261104
// BM: Sahkan empat pautan asal diurus library dengan ScrollSpy opt-in.
// EN: Confirm that the original four links are managed with opt-in ScrollSpy.
const navigationConfig = await read("./zrain-ui-config.js");
assert.match(navigationConfig, /scrollSpy:\s*true/);
for (const hash of ["features", "architecture", "playground", "quickstart"]) {
  // BM: Pautan anchor mesti mempunyai seksyen sebenar agar scrollSpy boleh mengesannya.
  // EN: Each anchor needs a real section so ScrollSpy can track it.
  assert.match(navigationConfig, new RegExp('href: "#' + hash + '"'));
  assert.match(html, new RegExp('id="' + hash + '"'));
}
// BM: Cegah cache browser daripada menggunakan versi shared UI lama.
// EN: Prevent browsers from reusing the stale shared UI references.
assert.match(html, /brand\.css\?v=a109401/);
assert.match(html, /zrain-ui-config\.js\?v=a109401/);
// BM: Pastikan peraturan mobile terhad kepada navigation yang diurus library.
// EN: Keep the mobile adjustment scoped to library-managed navigation.
const localStyles = await read("./site.css");
assert.match(localStyles, /ZRAIN-UI-AUTH-STARTER-MOBILE-20261104/);

console.log("Zrain Auth Starter shared UI checks passed.");
