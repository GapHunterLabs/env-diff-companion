# Demo — Env Diff Companion

`.env` (simula el archivo local real, con secretos de mentira) y
`.env.example` (simula la plantilla que se commitea) -- deliberadamente
desalineados para poder verificar el diff real.

## Pasos

1. En el Project View, seleccioná **ambos** archivos a la vez
   (`.env` y `.env.example`, click en uno + `Ctrl+Click` en el otro).
2. Click derecho → **Compare Env Files**.
3. Debería abrirse `env-diff-report.md` con el resultado.

## Resultado esperado

- **Solo en `.env`** (falta en `.env.example`): `LOCAL_ONLY_FLAG`
- **Solo en `.env.example`** (falta en `.env`): `NEW_FEATURE_FLAG`,
  `SENTRY_DSN`
- **En ambos** (no debería aparecer como diferencia, aunque los
  VALORES sean distintos -- esto es un diff de presencia, no de
  valores): `DATABASE_URL`, `API_KEY`, `DEBUG`, `REDIS_URL`
- La línea comentada (`# API_KEY=...`) NO debería contar como una
  key real.
- `DATABASE_URL` aparece 2 VECES en `.env` (duplicado a propósito) --
  no debería aparecer duplicado en el reporte ni confundir el conteo.
- `export REDIS_URL=...` debería contar como la key `REDIS_URL`, sin
  el `export ` pegado al nombre.

## Qué reportar

- ¿El reporte generado coincide exactamente con lo de arriba?
- ¿Correr la acción de nuevo sobre el mismo par sobrescribe el reporte
  limpio, o lo duplica/acumula?
- ¿Qué pasa si seleccionás solo 1 archivo, o 3? (la acción debería
  quedar deshabilitada -- confirmá que el menú ni siquiera la ofrece)
