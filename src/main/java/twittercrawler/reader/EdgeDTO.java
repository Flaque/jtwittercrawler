package twittercrawler.reader;

public class EdgeDTO {

	private int from, to;

	public EdgeDTO(int from, int to) {
		this.from = from;
		this.to = to;
	}

	public int getFrom() {
		return from;
	}

	public int getTo() {
		return to;
	}

	@Override
	public String toString() {
		return from + ":" + to;
	}
}