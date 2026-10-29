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
console.log("Zrain Auth Starter shared UI checks passed.");
