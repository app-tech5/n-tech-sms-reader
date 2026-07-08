# N-TECH SMS Reader — M1.1

Mini-app Android : réception SMS en **arrière-plan**, affichage brut (expéditeur + texte + date).

## Lien client (téléchargement direct APK)

```
https://prospect.fromcm.com/ntech/downloads/ntech-sms-reader.apk
```

Le client ouvre ce lien sur son téléphone Android → téléchargement → installation.

---

## Setup VPS (une seule fois)

### 1. Dossier sur le serveur

```bash
sudo mkdir -p /var/www/ntech/downloads
sudo chown $USER:www-data /var/www/ntech/downloads
```

### 2. Nginx

Ajouter le contenu de `deploy/nginx-downloads.conf` dans la config nginx de `prospect.fromcm.com`, puis :

```bash
sudo nginx -t && sudo systemctl reload nginx
```

### 3. Secrets GitHub (repo n-tech-sms-reader)

| Secret | Exemple |
|--------|---------|
| `VPS_HOST` | `prospect.fromcm.com` |
| `VPS_USER` | `deploy` |
| `VPS_SSH_KEY` | clé privée SSH |
| `VPS_PORT` | `22` |
| `VPS_APK_DIR` | `/var/www/ntech/downloads/` |

À chaque push sur `main`, la CI build l'APK et l'envoie sur le VPS. Le lien client reste le même.

---

## Installation sur le téléphone marchand

1. Ouvrir le lien ci-dessus sur le téléphone
2. Autoriser « sources inconnues » si demandé
3. Installer
4. À l'ouverture : **Autoriser les SMS**
5. Batterie → désactiver l'optimisation pour **N-TECH SMS**

## Test client M1.1

| # | Action | Résultat attendu |
|---|--------|------------------|
| 1 | Installer l'app, **ne pas l'ouvrir** | — |
| 2 | Envoyer un SMS test au numéro marchand | — |
| 3 | Attendre 30 s, ouvrir l'app | SMS visible : expéditeur, texte, date/heure |
| 4 | Envoyer un 2e SMS | Les 2 apparaissent dans la liste |

**Validé si :** les 2 SMS sont corrects, captés sans avoir gardé l'app ouverte.

## Hors périmètre M1.1

- Filtre Orange / MTN
- Parsing montant
- Envoi au backend
