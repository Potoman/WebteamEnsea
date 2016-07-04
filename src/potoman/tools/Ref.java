package potoman.tools;

public class Ref<T> {
	T t = null;
	
	public Ref(T t) {
		this.t = t;
	}
	
	public void set(T t) {
		this.t = t;
	}
	
	public T get() {
		return t;
	}
}
