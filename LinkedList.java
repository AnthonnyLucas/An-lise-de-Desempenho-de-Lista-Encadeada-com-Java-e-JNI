public class LinkedList {
    private Node head;

    public LinkedList() {
        this.head = null;
    }

    public void adicionar(double data, int position) {
        Node newNode = new Node(data);

        if (position < 0) {
            System.out.println("ERRO: Posicao " + position + " e invalida.");
            return;
        }

        if (position == 0) {
            newNode.next = head;
            head = newNode;
            System.out.println("INFO: Valor " + data + " adicionado na posicao " + position + ".");
            return;
        }

        Node currentNode = head;
        int currentPosition = 0;

        while (currentNode != null && currentPosition < position - 1) {
            currentNode = currentNode.next;
            currentPosition++;
        }

        if (currentNode == null) {
            System.out.println("ERRO: Posicao " + position + " e invalida ou fora do alcance da lista.");
        } else {
            newNode.next = currentNode.next;
            currentNode.next = newNode;
            System.out.println("INFO: Valor " + data + " adicionado na posicao " + position + ".");
        }
    }

    public void remover(double data) {
        if (head == null) {
            System.out.println("ERRO: Nao e possivel remover o valor " + data + ", pois a lista esta vazia.");
            return;
        }

        if (head.data == data) {
            head = head.next;
            System.out.println("INFO: Valor " + data + " removido da lista.");
            return;
        }

        Node currentNode = head;
        Node previousNode = null;

        while (currentNode != null && currentNode.data != data) {
            previousNode = currentNode;
            currentNode = currentNode.next;
        }

        if (currentNode == null) {
            System.out.println("ERRO: Valor " + data + " nao encontrado na lista.");
        } else {
            previousNode.next = currentNode.next;
            System.out.println("INFO: Valor " + data + " removido da lista.");
        }
    }

    public void imprimirLista() {
        System.out.print("Lista atual: ");
        if (head == null) {
            System.out.println("(vazia)");
            return;
        }
        
        Node currentNode = head;
        while (currentNode != null) {
            System.out.print(currentNode.data + " ");
            currentNode = currentNode.next;
        }
        System.out.println();
    }
}