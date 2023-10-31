import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		Buffer buffer = new Buffer();

		Thread productor1 = new Thread(new Productor(buffer, "Productor 1"));
		Thread productor2 = new Thread(new Productor(buffer, "Productor 2"));
		Thread productor3 = new Thread(new Productor(buffer, "Productor 3"));
		Thread consumidor1 = new Thread(new Consumidor(buffer, "Consumidor 1"));
		Thread consumidor2 = new Thread(new Consumidor(buffer, "Consumidor 2"));

		productor1.start();
		productor2.start();
		productor3.start();
		consumidor1.start();
		consumidor2.start();
	}
}

class Email {
	private int id;
	private String destinatario;
	private String remitente;
	private String asunto;
	private String cuerpo;

	public Email(int id, String destinatario, String remitente, String asunto, String cuerpo) {
		this.id = id;
		this.destinatario = destinatario;
		this.remitente = remitente;
		this.asunto = asunto;
		this.cuerpo = cuerpo;
	}

	public int getId() {
		return id;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public String getRemitente() {
		return remitente;
	}

	public String getAsunto() {
		return asunto;
	}

	public String getCuerpo() {
		return cuerpo;
	}
}

class Buffer {
	
	//se recomendaría utilizar una estructura de datos thread-safe como BlockingQueue
	
    private List<Email> emails = new ArrayList<>();

    public void depositarEmail(Email email, String nombreProductor) {
        if (!email.getDestinatario().equals("pikachu@gmail.com")) {
            emails.add(email);
            System.out.println(nombreProductor + " depositó el email con ID " + email.getId());
        } else {
            System.out.println("Descartando email para " + email.getDestinatario() + " por " + nombreProductor);
        }
    }

    public Email tomarEmail(String nombreConsumidor) {
        if (emails.isEmpty()) {
            return null;
        }

        Email email = emails.remove(0);
        System.out.println(nombreConsumidor + " envió el email con ID " + email.getId() + " a " + email.getDestinatario());
        return email;
    }
}


class Productor implements Runnable {
	private Buffer buffer;
	private String nombreProductor;

	public Productor(Buffer buffer, String nombreProductor) {
		this.buffer = buffer;
		this.nombreProductor = nombreProductor;
	}

	@Override
	public void run() {
		for (int i = 1; i <= 10; i++) {
			Email email = new Email(i, "destinatario" + i + "@example.com", "remitente" + i + "@example.com",
					"Asunto" + i, "Cuerpo del email " + i);
			buffer.depositarEmail(email, nombreProductor);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}

class Consumidor implements Runnable {
	private Buffer buffer;
	private String nombreConsumidor;

	public Consumidor(Buffer buffer, String nombreConsumidor) {
		this.buffer = buffer;
		this.nombreConsumidor = nombreConsumidor;
	}

	@Override
	public void run() {
		while (true) {
			Email email = buffer.tomarEmail(nombreConsumidor);
		}
	}
}


