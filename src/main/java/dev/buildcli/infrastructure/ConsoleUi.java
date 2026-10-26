package dev.buildcli.infrastructure;

import dev.buildcli.domain.Event;
import dev.buildcli.ports.ApprovalRequest;
import dev.buildcli.ports.EscalationChoice;
import dev.buildcli.ports.UserInterface;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public final class ConsoleUi implements UserInterface {

    public enum Policy {
        ASK,
        ALLOW_ALL,
        DENY_ALL
    }

    private final BufferedReader reader;
    private final PrintStream out;
    private final Policy policy;

    public ConsoleUi(Policy policy) {
        this(new BufferedReader(new InputStreamReader(System.in)), System.out, policy);
    }

    public ConsoleUi(BufferedReader reader, PrintStream out, Policy policy) {
        this.reader = reader;
        this.out = out;
        this.policy = policy;
    }

    @Override
    public void onEvent(Event event) {
        out.println("[EVENT] " + event.description());
    }

    @Override
    public boolean askApproval(ApprovalRequest request) {
        if (policy == Policy.ALLOW_ALL) return true;
        if (policy == Policy.DENY_ALL) return false;

        out.print(request.prompt() + " (y/N): ");
        try {
            String line = reader.readLine();
            return line != null && line.trim().equalsIgnoreCase("y");
        } catch (IOException e) {
            return false; // Nega em caso de erro na leitura
        }
    }
}
