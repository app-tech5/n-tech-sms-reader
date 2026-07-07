# N-TECH SMS Reader — M1.1

Mini-app Android : réception SMS en **arrière-plan**, affichage brut (expéditeur + texte + date).

## Livrable M1.1

- APK `ntech-sms-reader-v0.1.apk`
- Réception SMS même app fermée
- Liste locale des SMS reçus (tous expéditeurs, pas de filtre Orange/MTN)

## Build (GitHub Actions — pas de build local)

1. Pousser le code sur GitHub
2. Aller dans **Actions** → workflow **Build APK** → **Run workflow** (ou push sur `main`)
3. À la fin du job, télécharger l’artifact **`ntech-sms-reader-v0.1`**

L’APK s’appelle : `ntech-sms-reader-v0.1.apk`

### Release (optionnel)

Créer un tag `v0.1.0` → l’APK est aussi attaché à la GitHub Release :

```bash
git tag v0.1.0
git push origin v0.1.0
```

## Installation sur le téléphone marchand

1. Copier l'APK sur le téléphone
2. Autoriser « sources inconnues » si demandé
3. Installer
4. À l'ouverture : **Autoriser les SMS**
5. Paramètres téléphone → Batterie → désactiver l'optimisation pour **N-TECH SMS** (recommandé)

## Test client M1.1

| # | Action | Résultat attendu |
|---|--------|------------------|
| 1 | Installer l'app, **ne pas l'ouvrir** (ou la fermer) | — |
| 2 | Depuis un autre téléphone, envoyer un SMS texte au numéro marchand | — |
| 3 | Attendre 30 s, ouvrir l'app | SMS visible : expéditeur, texte, date/heure |
| 4 | Envoyer un 2e SMS | Les 2 apparaissent dans la liste |

**Validé si :** les 2 SMS sont corrects, captés sans avoir gardé l'app ouverte.

## Structure

```
app/src/main/java/com/ntech/smsreader/
  MainActivity.kt      # Liste + permissions
  SmsReceiver.kt       # Réception background
  SmsDatabaseHelper.kt # Stockage local SQLite
  SmsRecord.kt         # Modèle
```

## Hors périmètre M1.1

- Filtre Orange / MTN (M1.2 / M1.3)
- Parsing montant (M1.4)
- Envoi au backend
