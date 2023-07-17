# OpenAI Vaadin Client *REQUIRES API KEY

Um die Services von OpenAI verwenden zu können wird ein API Key benötigt.

Dieser muss entweder in der application.yml unter dem Wert apikey abgelegt werden oder

er wird im AWS Systems Manager Parameter Store von Amazon hinterlegt.

## Set Up AWS

Erstelle in Deinem OS-Benutzer-Verzeichnis unter /.aws/credentials einen Eintrag für deinen perssönlichen Parameter Store, z.B. so:

```
[test]
aws_access_key_id=AK000000000000000074
aws_secret_access_key=XYZ
```

Da es sich hierbei nicht um das [default] Profil handelt müssen bei Start die noch die Umgebungsvariablen entsprechend gesetzt werden:

![img.png](img.png)
AWS_PROFILE=test;AWS_REGION=eu-central-1

Wichtig ist die Region nicht zu vergessen, da es andernfalls nicht funktioniert. Nach dem Start kann über das Terminal geprüft werden, ob der Key stimmt, siehe:

```------------------- API Key -> XXX``` (sollte in Produktion deaktiviert werden)

## ChatGPT

API: https://platform.openai.com/docs/api-reference/chat

Die Adresse lautet: http://localhost:8080/chat

