function generateKeyPair() {
    var rsa = forge.pki.rsa;
    return rsa.generateKeyPair({bits: 2048, e: 0x10001});
}

function decrypt(data, keypair) {


    var data2 = data.cle;

    console.info("data2", data2);

    var decoded = forge.util.decode64(data2);

    console.info("decoded", decoded);

    var res2 = keypair.privateKey.decrypt(decoded, 'RSA-OAEP', {
        md: forge.md.sha256.create(),
        mgf1: {
            md: forge.md.sha1.create()
        }
    });

    console.info("res2", res2);

    console.info("iv", data.iv);
    console.info("key", res2);
    console.info("encrypted", data.reponse);

    var iv = forge.util.decode64(data.iv);
    var key2 = res2;
    var encrypted = forge.util.decode64(data.reponse);

    var decipher = forge.cipher.createDecipher('AES-CBC', key2);
    decipher.start({iv: iv});
    decipher.update(forge.util.createBuffer(encrypted));
    var result = decipher.finish(); // check 'result' for true/false
    console.log("result", result);
    // outputs decrypted hex
    console.log(decipher.output.toHex());
    console.log("decrypte", decipher.output);

    if (decipher.output && decipher.output.data) {
        return decipher.output.data;
    }

    return '';
}

function test(model) {

    console.log("coucou2");

    var password = model.password;
    model.password = '';

    var keypair = generateKeyPair();

    var pem = forge.pki.publicKeyToPem(keypair.publicKey);

    var demande = {
        password: forge.util.encode64(password),
        cle: forge.util.encode64(pem)
    };

    $.ajax({
        url: '/api/message',
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            console.info("reponse", data);

            var message = decrypt(data, keypair);

            if (message) {
                model.message = message;
            } else {
                model.messageErreur = "Erreur pour récupérer le message";
            }
        },
        error: function (data) {
            console.info("Erreur", data);
            model.messageErreur = "Erreur pour récupérer le message";
        },
        data: JSON.stringify(demande)
    });

}


function main() {
    var app = new Vue({
        el: '#app',
        data: {
            message: '',
            messageErreur: '',
            password: ''
        },
        methods: {
            connexion: function () {

                console.log("coucou");

                try {

                    this.messageErreur = '';

                    if (!this.password || this.password === '') {
                        this.messageErreur = "Le mot de passe n'est pas renseigné";
                    } else {
                        test(this);
                    }

                } catch (e) {
                    this.messageErreur = "Erreur pour récupérer le mot de passe";
                }

            },
            reinit: function () {
                this.message = '';
                this.messageErreur = '';
                this.password = '';
            }
        }
    });
}

main();

