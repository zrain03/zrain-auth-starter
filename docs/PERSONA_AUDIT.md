<!--
Pembangun / Developer: Zrain
Dicipta / Created: 2026-11-04
Dikemas kini / Last updated: 2026-11-04
Fail / File: docs/PERSONA_AUDIT.md
BM: Rekod skop audit, pengecualian dan perkara yang belum disahkan.
EN: Record audit scope, exclusions and remaining unverified work.
-->

# Zrain Auth Starter: Persona audit

## Verified source-of-truth / Sumber yang diperiksa
- Base revision: `07f303b77694cbf7785bb3ef0fb2e48f101a1764`.
- Persona: Zrain Ultimate Engineering Master bilingual edition, 4 November 2026.
- Creation-date evidence: Git initial commit on 26 October 2026 and file introduction history.
- Shared UI pin remains `a109401fa591afa9f66479fa8d86f1ed1679d70b`; do not alter its copied files separately.

## Decisions / Keputusan
- **BM:** Tidak ada fail yang disahkan selamat untuk dipadam pada skop ini; hapus tanpa bukti boleh merosakkan GitHub Pages, Maven Wrapper, atau aliran update UI.
- **EN:** No file was proven safe to delete in this scope; unverified deletion could break GitHub Pages, Maven Wrapper or the UI update workflow.
- **BM:** Kod executable Java, JWT, database dan demo sengaja tidak diubah; perubahan ini mengutamakan provenance dan penerangan yang boleh disemak.
- **EN:** Executable Java, JWT, database and demo logic remain unchanged; this change prioritizes reviewable provenance and explanations.
- **BM:** Salinan `docs/brand.css`, `docs/zrain-ui.js` dan `docs/zrain-logo.webp` mesti dikekalkan byte-for-byte agar `ui:check` lulus.
- **EN:** Those three copied assets must remain byte-for-byte identical so `ui:check` passes.
- **BM:** Kod Maven Wrapper dan teks lesen tidak diubah serta dibezakan daripada hasil asli Zrain.
- **EN:** Maven Wrapper and license text remain untouched and distinguished from original Zrain work.

## Verification / Pengesahan
- Run `node scripts/check-persona.mjs`.
- Run `npm run test:ui`.
- Run `./mvnw -B clean verify` (Linux/macOS) or `.\mvnw.cmd clean verify` (Windows).
- **BM:** Keputusan CI dan browser hanya boleh dianggap lulus selepas run sebenar selesai.
- **EN:** CI and browser results must only be considered passed after real execution.

## Follow-up / Susulan
- **BM:** Untuk audit komen semantik menyeluruh, semak penerangan blok dan fungsi secara manual; kiraan header bukan bukti mutlak kualiti dokumentasi.
- **EN:** For complete semantic comment review, inspect block and function explanations manually; header counts alone are not proof of documentation quality.
