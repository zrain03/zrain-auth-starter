/**
 * Fail / File: scripts/check-persona.mjs
 * Pembangun / Developer: Zrain
 * Dicipta / Created: 2026-11-04
 * Dikemas kini / Last updated: 2026-11-04
 * BM: Periksa inventori provenance dan header tanpa mengubah source atau salinan shared UI.
 * EN: Verify provenance inventory and headers without modifying source or copied shared UI.
 */
import assert from "node:assert/strict";
import {execFileSync} from "node:child_process";
import {readFileSync} from "node:fs";

// BM: Gunakan fail tracked, bukan traversal node_modules atau fail binaan sementara.
// EN: Use tracked files, not node_modules or transient build directories.
const tracked = execFileSync("git", ["ls-files", "-z"], {encoding:"utf8"})
  .split("\0").filter(Boolean).sort();
const data = JSON.parse(readFileSync("FILE_PROVENANCE.json", "utf8"));
assert.equal(data.maintainer, "Zrain");
assert.equal(data.schemaVersion, 1);
const records = data.files;
assert.ok(Array.isArray(records));

// BM: Registry wajib meliputi setiap fail termasuk dirinya sendiri tanpa hash kendiri.
// EN: The registry must cover every file including itself, without self-referential hashes.
const registered = records.map(item=>item.path).sort();
assert.deepEqual(registered,tracked, "Provenance registry must match every tracked file");
assert.equal(new Set(registered).size,registered.length,"Duplicate provenance entries");

const excluded = new Set(["strict-json","upstream-copy","upstream-managed","third-party-license","binary-asset","sentinel"]);
const iso = /^20\d\d-\d\d-\d\d$/;
let inline = 0;
for (const entry of records) {
  assert.equal(entry.maintainer, "Zrain", entry.path);
  assert.match(entry.created,iso, entry.path+" created date");
  assert.match(entry.lastUpdated,iso, entry.path+" updated date");
  assert.ok(entry.createdEvidence?.trim(), entry.path+" provenance evidence");
  if (excluded.has(entry.commentPolicy)) continue;
  assert.equal(entry.commentPolicy,"inline",entry.path+" comment policy");
  const text=readFileSync(entry.path,"utf8").slice(0,2000);
  // BM: Komen awal perlu mengandungi developer, tarikh dan penerangan dalam susunan BM/EN.
  // EN: Early comments must include developer, dates and descriptions in BM/EN order.
  assert.match(text,/Pembangun\s*\/\s*Developer:\s*Zrain/,entry.path+" developer header");
  assert.match(text,/(?:Dicipta|Tarikh dicipta)\s*\/\s*Created:\s*20\d\d-\d\d-\d\d/,entry.path+" created header");
  assert.match(text,/(?:Dikemas kini|Tarikh dikemas kini)\s*\/\s*Last updated:\s*20\d\d-\d\d-\d\d/,entry.path+" updated header");
  const bm=text.search(/(?:^|\n)\s*(?:[\*#]\s*)?BM:/m);
  const en=text.search(/(?:^|\n)\s*(?:[\*#]\s*)?EN:/m);
  assert.ok(bm>=0&&en>bm,entry.path+" bilingual header order");
  inline++;
}
// BM: Ini audit struktur, bukan bukti bahawa semua fungsi telah diuji secara runtime.
// EN: This is a structural audit, not proof that every function passed runtime testing.
console.log("Zrain Persona structural audit passed: "+tracked.length+" tracked files, "+inline+" inline-comment files.");
