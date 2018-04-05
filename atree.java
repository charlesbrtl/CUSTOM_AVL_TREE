
/*Charles Letonnellier de Breteuil
 * 27296592
 * COMP 352
 * assignment 3
 * */
import java.io.File;
import java.util.Scanner;
public class atree extends BinaryTree {
	static int comparisons;
	static int parentchange;

	atree() {
		this.root = null;
	}

	private class AVLNode extends BinaryTree.Node {
		int height;

		AVLNode(int data) {
			super();
			this.data = data;
			this.height = 1;
		}
	}

	public void add(int key) {
		parentchange++;
		root = add((AVLNode) root, key);
	}

	public void remove(int key) {
		root = remove((AVLNode) root, key);
	}

	public void search(int key) {
		root = search((AVLNode) root, key);
	}

	private AVLNode add(AVLNode node, int key) {
		if (node == null) {
			return new AVLNode(key);
		}
		if (key < node.data) {
			comparisons++;
			node.left = add((AVLNode) node.left, key);
		} else if (key > node.data) {
			comparisons++;
			node.right = add((AVLNode) node.right, key);
		} else {
			return node;
		}

		NewHeight(node);
		if (Difference(node) > 1) {
			if (key < node.left.data) {
				comparisons++;
				node = rotateRight(node);
			} else {
				comparisons++;
				node.left = rotateLeft((AVLNode) node.left);
				node = rotateRight(node);
			}
		} else if (Difference(node) < -1) {
			if (key > node.right.data) {
				comparisons++;
				node = rotateLeft(node);
			} else {
				comparisons++;
				node.right = rotateRight((AVLNode) node.right);
				node = rotateLeft(node);
			}
		}
		return node;
	}

	private AVLNode remove(AVLNode node, int key) {
		if (node == null)
			return null;

		if (key < node.data) {
			comparisons++;
			node.left = remove((AVLNode) node.left, key);
		} else if (key > node.data) {
			comparisons++;
			node.right = remove((AVLNode) node.right, key);
		} else {
			if (node.left == null) {
				comparisons++;
				node = (AVLNode) node.right;
			} else if (node.right == null) {
				comparisons++;
				node = (AVLNode) node.left;
			} else {
				int inorderSuccessorValue = LeftMost((AVLNode) node.right);
				node.data = inorderSuccessorValue;
				node.right = remove((AVLNode) node.right, inorderSuccessorValue);
			}
		}
		if (node == null) {
			return null;
		}
		NewHeight(node);
		int balance = Difference(node);

		if (balance > 1) {
			if (Difference((AVLNode) node.left) >= 0) {
				node = rotateRight(node);
			} else {
				node.left = rotateLeft((AVLNode) node.left);
				node = rotateRight(node);
			}
		} else if (balance < -1) {
			if (Difference((AVLNode) node.right) <= 0) {
				node = rotateLeft(node);
			} else {
				node.right = rotateRight((AVLNode) node.right);
				node = rotateLeft(node);
			}
		}
		return node;
	}

	private AVLNode search(AVLNode current, int key) {
		comparisons++;
		if (current.data < key)
			search((AVLNode) current.right, key);
		else if (current.data > key)
			search((AVLNode) current.left, key);
		return current;

	}

	private int Heightof(AVLNode node) {
		if (node == null)
			return 0;
		return node.height;
	
	}

	private void NewHeight(AVLNode... node) {
		for (AVLNode n : node) {
			if (node == null)
				return;

			n.height = Math.max(Heightof((AVLNode) n.left), Heightof((AVLNode) n.right)) + 1;
		}
	}

	private int Difference(AVLNode node) {
		if (node == null) {
			return 0;
		} else {
			return Heightof((AVLNode) node.left) - Heightof((AVLNode) node.right);
		}
	}

	private AVLNode rotateRight(AVLNode node) {
		parentchange+=2;
		if (node == null)
			return node;

		AVLNode temp = (AVLNode) node.left;

		AVLNode othertemp = (AVLNode) temp.right;

		temp.right = node;
		node.left = othertemp;
		NewHeight(node, temp);
		return temp;
	}

	private AVLNode rotateLeft(AVLNode node) {
		parentchange+=2;
		if (node == null)
			return node;
		else {
			AVLNode temp = (AVLNode) node.right;
			AVLNode othertemp = (AVLNode) temp.left;
			temp.left = node;
			node.right = othertemp;
			NewHeight(node, temp);
			return temp;
		}
	}

	private int LeftMost(AVLNode node) {
		if (node == null)
			return 0;
		if (node.left == null)
			return node.data;
		return LeftMost((AVLNode) node.left);
	}

	public static void main(String[] args) {
		atree tree = new atree();
		int remove = 0;
		int add = 0;
		int find = 0;
		File file = new File(new File(args[0].substring(1, args[0].length()-1)).getAbsolutePath());
		try {
			Scanner scan = new Scanner(file);

			while (scan.hasNextLine()) {
				String operation = scan.nextLine();
				int number = Integer.parseInt(operation.substring(1, operation.length()));
				switch (operation.charAt(0)) {
				case 'r':
					tree.remove(number);
					remove++;
					break;
				case 'a':
					tree.add(number);
					add++;
					break;
				case 'f':
					try {
						tree.search(number);
					} catch (NullPointerException e) {
						//because sometimes, the nodes just aren't there ;)
					}
					find++;
					break;
				}

			}
			scan.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		System.out.println("AVL Tree Statistics:");
		System.out.println("Comparisons: " + comparisons);
		System.out.println("Parent changes: " + parentchange);
		System.out.println("Insertions: " + add);
		System.out.println("Removals: " + remove);
		System.out.println("Searches: " + find);
	}
}
