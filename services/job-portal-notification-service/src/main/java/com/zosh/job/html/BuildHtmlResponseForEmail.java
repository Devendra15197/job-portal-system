package com.zosh.job.html;

import com.zosh.job.domain.ApplicationStatus;
import com.zosh.job.event.ApplicationStatusChangedEvent;
import org.springframework.stereotype.Component;

@Component
public class BuildHtmlResponseForEmail {

    public String buildStatusChangeEmailHtml() {
        return "<html>" +
                "<body>" +
                "<h2>Job Application Status Update</h2>" +
                "<p>Dear Applicant,</p>" +
                "<p>Your job application status has been updated. Please log in to your account to view the details.</p>" +
                "<p>Best regards,<br/>Job Portal Team</p>" +
                "</body>" +
                "</html>";
    }


    public String buildStatusChangeEmailHtml(ApplicationStatusChangedEvent event) {
        String candidateName = escapeHtml(
                event.getCandidateName() != null
                        ? event.getCandidateName()
                        : "Candidate"
        );

        String jobTitle = escapeHtml(
                event.getJobTitle() != null
                        ? event.getJobTitle()
                        : "Job opportunity"
        );

        String companyName = escapeHtml(
                event.getCompanyName() != null
                        ? event.getCompanyName()
                        : "the hiring company"
        );

        String oldStatus = escapeHtml(
                event.getOldStatus() != null
                        ? event.getOldStatus().name().replace("_", " ")
                        : "UNKNOWN"
        );

        String newStatus = escapeHtml(
                event.getNewStatus() != null
                        ? event.getNewStatus().name().replace("_", " ")
                        : "UPDATED"
        );

        String note = event.getNote() == null
                || event.getNote().isBlank()
                ? ""
                : """
                <div style="margin-top:24px;padding:16px;
                            background-color:#f8fafc;
                            border-left:4px solid #6366f1;
                            border-radius:6px;">
                    <p style="margin:0 0 8px;color:#475569;
                              font-size:13px;font-weight:600;">
                        NOTE FROM THE EMPLOYER
                    </p>
                    <p style="margin:0;color:#334155;
                              font-size:14px;line-height:1.6;">
                        %s
                    </p>
                </div>
                """.formatted(escapeHtml(event.getNote()));

        String statusColor = getStatusColor(event.getNewStatus());

        String changedAt = event.getChangedAt() == null
                ? ""
                : """
                <p style="margin:8px 0 0;color:#64748b;
                          font-size:12px;">
                    Updated on: %s
                </p>
                """.formatted(
                escapeHtml(event.getChangedAt().toString()
                        .replace("T", " "))
        );

        return """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport"
                  content="width=device-width, initial-scale=1.0">
            <title>Application Status Update</title>
        </head>
        <body style="margin:0;padding:0;background-color:#f1f5f9;
                     font-family:Arial,Helvetica,sans-serif;">

            <table role="presentation" width="100%%" cellpadding="0"
                   cellspacing="0" border="0"
                   style="background-color:#f1f5f9;padding:32px 12px;">
                <tr>
                    <td align="center">

                        <table role="presentation" width="600"
                               cellpadding="0" cellspacing="0" border="0"
                               style="max-width:600px;width:100%%;
                                      background-color:#ffffff;
                                      border-radius:12px;
                                      overflow:hidden;">

                            <!-- Header -->
                            <tr>
                                <td style="background-color:#312e81;
                                           padding:28px 32px;">
                                    <p style="margin:0;color:#c7d2fe;
                                              font-size:12px;
                                              font-weight:700;
                                              letter-spacing:2px;">
                                        JOB PORTAL
                                    </p>
                                    <h1 style="margin:12px 0 0;
                                               color:#ffffff;
                                               font-size:25px;
                                               line-height:1.3;">
                                        Application Status Update
                                    </h1>
                                </td>
                            </tr>

                            <!-- Main content -->
                            <tr>
                                <td style="padding:32px;">

                                    <p style="margin:0 0 16px;
                                              color:#0f172a;
                                              font-size:16px;">
                                        Hi <strong>%s</strong>,
                                    </p>

                                    <p style="margin:0 0 24px;
                                              color:#475569;
                                              font-size:14px;
                                              line-height:1.7;">
                                        There's an update regarding your
                                        job application. Here are the
                                        details for your reference.
                                    </p>

                                    <!-- Job details -->
                                    <table role="presentation" width="100%%"
                                           cellpadding="0" cellspacing="0"
                                           border="0"
                                           style="background-color:#f8fafc;
                                                  border:1px solid #e2e8f0;
                                                  border-radius:8px;">
                                        <tr>
                                            <td style="padding:20px;">

                                                <p style="margin:0 0 6px;
                                                          color:#64748b;
                                                          font-size:12px;
                                                          font-weight:600;
                                                          text-transform:uppercase;">
                                                    Position
                                                </p>

                                                <h2 style="margin:0 0 6px;
                                                           color:#0f172a;
                                                           font-size:20px;">
                                                    %s
                                                </h2>

                                                <p style="margin:0;
                                                          color:#475569;
                                                          font-size:14px;">
                                                    %s
                                                </p>

                                                <p style="margin:12px 0 0;
                                                          color:#64748b;
                                                          font-size:12px;">
                                                    Application ID: #%s
                                                </p>

                                            </td>
                                        </tr>
                                    </table>

                                    <!-- Status change -->
                                    <div style="margin-top:28px;">
                                        <p style="margin:0 0 14px;
                                                  color:#0f172a;
                                                  font-size:15px;
                                                  font-weight:700;">
                                            Status change
                                        </p>

                                        <table role="presentation"
                                               cellpadding="0"
                                               cellspacing="0"
                                               border="0">
                                            <tr>
                                                <td style="padding:10px 12px;
                                                           background-color:#e2e8f0;
                                                           border-radius:6px;
                                                           color:#334155;
                                                           font-size:12px;
                                                           font-weight:700;">
                                                    %s
                                                </td>
                                                <td style="padding:0 12px;
                                                           color:#94a3b8;
                                                           font-size:20px;">
                                                    &rarr;
                                                </td>
                                                <td style="padding:10px 12px;
                                                           background-color:%s;
                                                           border-radius:6px;
                                                           color:#ffffff;
                                                           font-size:12px;
                                                           font-weight:700;">
                                                    %s
                                                </td>
                                            </tr>
                                        </table>
                                    </div>

                                    %s

                                    %s

                                    <p style="margin:28px 0 0;
                                              color:#475569;
                                              font-size:14px;
                                              line-height:1.7;">
                                        Thank you for using our job portal.
                                        We wish you the best in your
                                        career journey.
                                    </p>

                                </td>
                            </tr>

                            <!-- Footer -->
                            <tr>
                                <td style="padding:20px 32px;
                                           background-color:#f8fafc;
                                           border-top:1px solid #e2e8f0;">
                                    <p style="margin:0;
                                              color:#64748b;
                                              font-size:12px;
                                              line-height:1.6;">
                                        This is an automated notification
                                        about your job application.
                                        Please do not reply directly to
                                        this email.
                                    </p>
                                    <p style="margin:10px 0 0;
                                              color:#94a3b8;
                                              font-size:11px;">
                                        &copy; Job Portal
                                    </p>
                                </td>
                            </tr>

                        </table>

                    </td>
                </tr>
            </table>

        </body>
        </html>
        """.formatted(
                candidateName,
                jobTitle,
                companyName,
                event.getApplicationId() == null
                        ? "N/A"
                        : event.getApplicationId().toString(),
                oldStatus,
                statusColor,
                newStatus,
                note,
                changedAt
        );
    }

    private String getStatusColor(ApplicationStatus status) {
        if (status == null) {
            return "#475569";
        }

        return switch (status) {
            case SHORTLISTED -> "#15803d";
            case REJECTED -> "#b91c1c";
            case PENDING -> "#b45309";
            case INTERVIEW_SCHEDULED -> "#1d4ed8";
            case HIRED -> "#047857";
            case WITHDRAWN -> "#64748b";
            default -> "#475569";
        };
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

}
