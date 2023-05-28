package adeo.leroymerlin.cdp.domain.member;

public final class MemberTestUtils {
	private MemberTestUtils() {
	}

	public static Member createMember(String name) {
		Member member = new Member();
		member.setName(name);
		return member;
	}
}
