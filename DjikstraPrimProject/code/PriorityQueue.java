
public class PriorityQueue {

private PriorityNode head;

private void updateHead(PriorityNode newHead) {
    this.head = newHead;
}

class PriorityNode {
    private Object obj;
    private String name;
    private double value;
    private PriorityNode front;
    private PriorityNode back; 

    PriorityNode(Node queue_obj) {
        this.obj = queue_obj;
        this.value = queue_obj.getPriority();
        this.name =  queue_obj.getName();
        queue_obj.setPQNode(this);
    }
    PriorityNode(Edge queue_obj) {
        this.obj = queue_obj;
        this.value = queue_obj.getPriority();
        this.name = queue_obj.getName();
        queue_obj.setPQNode(this);
    }
    double getValue() {
        return value;
    }
    Object getObject() {
        return obj;
    }
    String getObjectName() {
        return name;
    }
    PriorityNode getFront() {
        return front;
    }
    PriorityNode getBack() {
        return back;
    }
    void updateValue(double newVal) {
        this.value = newVal;
        PriorityNode currfront = this.front;

        while (currfront != null) {
            if (currfront.getValue() <= this.value) {
                break;
            }
            else if (currfront.getValue() > this.value && (currfront == head || currfront.getFront().getValue() <= this.value)) {
                switchNodes(currfront, this);
                break;
            }
            else currfront = currfront.getFront();
        }
    }

    private void switchNodes(PriorityNode front, PriorityNode back) {
        PriorityNode frontfront = front.front; //could be null
        PriorityNode frontback = front.back;
        PriorityNode backfront = back.front;
        PriorityNode backback = back.back; //could be null
    
        back.front = frontfront;
        back.back = front;
        front.front = back;

        if (frontfront != null) {
            frontfront.back = back;
        }

        if (frontback == back) {
            front.back = backback;   
            if (backback != null) {
                backback.front = front;
            }   
        }
        
        else {
            backfront.back = backback;
            if (backback != null) {
                backback.front = backfront;
            }
        }

        if(back.front == null) {
            updateHead(back);
        }
    }
}

void printQueue() {
    PriorityNode current = head;

    while ( current != null ) {
        System.out.print(current.getObjectName() + ": " + current.getValue() + " -> ");
        current = current.getBack();
    }
    System.out.println("");
}

void enqueue(Object newObj) {
    PriorityNode newNode;

    if (newObj instanceof Node) {
        newNode = new PriorityNode((Node) newObj);
    }
    else if (newObj instanceof Edge) {
        newNode = new PriorityNode((Edge) newObj);
    }
    else return;

    if( this.head == null ) { 
        updateHead(newNode); 
    }
    else {
        PriorityNode next = this.head;
        
        while ( next != null) {
            if( next.getValue() <= newNode.getValue()) {
                
                if (next.getBack() == null) {
                    next.back =  newNode;
                    newNode.front = next; 
                    next = null;
                }
                else next = next.getBack();
            }
            else {
                PriorityNode nextfront = next.getFront();
                next.front = newNode;
                newNode.back = next;
                newNode.front = nextfront;

                if (newNode.front != null) {
                    newNode.front.back = newNode;
                }
                else {
                    updateHead(newNode);
                }
                next = null;
            }
        }
    }
}

Object pop() {
    PriorityNode returnNode; 
    if( head == null) {
        return null;
    }
    returnNode = head;
    head = head.back;
    if( head != null) {
        head.front = null;
    }
    return returnNode.getObject();
}

boolean isEmpty() {
    return head == null;
}

}