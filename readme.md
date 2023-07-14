# OpenAI Vaadin Client



Um die Services von OpenAI verwenden zu können wird ein API Key benötigt. Dieser ist im AWS Systems Manager Parameter Store zu hinterlegen.

## Set Up

Erstelle in Deinem OS-Benutzer-Verzeichnis unter /.aws/credentials einen Eintrag für deinen perssönlichen Parameter Store, z.B. so:

```
[personal]
aws_access_key_id=AK000000000000000074
aws_secret_access_key=XYZ
```

## ChatGPT

API: https://platform.openai.com/docs/api-reference/chat

Die Adresse lautet: http://localhost:8080/chat

