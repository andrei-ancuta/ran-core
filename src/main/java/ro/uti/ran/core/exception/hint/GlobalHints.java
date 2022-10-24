package ro.uti.ran.core.exception.hint;

public interface GlobalHints {
    String COMPRESS_ALGORITM_HINT = "Compresia datelor se va realiza folosind algoritmul ZLIB (RFC 1950)";

    String DIGITAL_SIGNATURE_HINT = "Autenticitate unui mesaj se realizeaza prin semnarea mesajului relevant utilizand standardul PKCS7, mecanismul de semnatura detasata.\n" +
            "Semnatura digitala se aplica asupra vectorului de octeti obtinut dupa compresia ZLIB. Semnatura include doar certificatul utilizat pentru semnare (nu intregul lant de certificate)";

    String DIGITAL_SIGNATURE_CORRUPT_HINT = "S-a realizat cu succes extragerea semnatarului dar nu a putut fi validata semnatura cu acesta.\n" +
            "Este posibil ca datele sa fie alterate";

}
