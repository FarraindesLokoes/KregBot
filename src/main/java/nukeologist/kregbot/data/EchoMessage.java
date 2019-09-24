package nukeologist.kregbot.data;

import net.dv8tion.jda.api.entities.Member;
import nukeologist.kregbot.api.Context;

import java.util.Objects;

public class EchoMessage {

    private final long OWNER;
    private String LABEL;
    private String messageContent;

    public EchoMessage(final long OWNER, final String LABEL) {
        this.OWNER = OWNER;
        this.LABEL = LABEL;
    }

    public final EchoMessage updateContent(final String messageContent) {
        this.messageContent = messageContent;
        return this;
    }

    public final String getMessageContent() {
        return messageContent;
    }

    public final String getLabel() {
        return LABEL;
    }

    public final void setLabel(String label) {
        this.LABEL = label;
    }

    public final Member getOwner(Context ctx) {
        return ctx.getMember().getGuild().getMemberById(OWNER);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof EchoMessage)) return false;
        final EchoMessage echo = (EchoMessage) obj;
        if (echo.OWNER != this.OWNER) return false;
        if (!echo.LABEL.equals(this.LABEL)) return false;
        return messageContent.equals(echo.messageContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(OWNER, LABEL, messageContent);
    }
}
