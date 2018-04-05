/*Charles Letonnellier de Breteuil
 * 27296592
 * COMP 352
 * assignment 3
 * */
public abstract class BinaryTree {
	Node root;

	BinaryTree() {
		this.root = null;
	}

	protected class Node {
		int data;
		Node right;
		Node left;

		public Node() {
		}

		Node(int data) {
			this.data = data;
		}
	}
}
