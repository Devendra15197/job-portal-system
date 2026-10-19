
package com.zosh.job.html;

import com.zosh.job.domain.ApplicationStatus;
import com.zosh.job.event.ApplicationStatusChangedEvent;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

@Component
public class BuildHtmlResponseForEmail {

    private static final String BRAND_NAME = "JOB PORTAL";
    private static final String NAVY = "#14243A";
    private static final String TEXT_PRIMARY = "#172B4D";
    private static final String TEXT_SECONDARY = "#475569";
    private static final String BORDER = "#E2E8F0";
    private static final String PAGE_BACKGROUND = "#F4F6F9";

    public String buildStatusChangeEmailHtml() {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport"
                          content="width=device-width, initial-scale=1.0">
                    <title>Application Status Update</title>
                </head>
                <body style="margin:0;padding:0;
                             background-color:%s;
                             font-family:Arial,Helvetica,sans-serif;">
                    <table role="presentation" width="100%%"
                           cellspacing="0" cellpadding="0" border="0"
                           style="background-color:%s;padding:36px 12px;">
                        <tr>
                            <td align="center">
                                <table role="presentation" width="600"
                                       cellspacing="0" cellpadding="0"
                                       border="0"
                                       style="width:100%%;max-width:600px;
                                              background:#ffffff;
                                              border:1px solid %s;">
                                    <tr>
                                        <td style="background-color:%s;
                                                   padding:32px;">
                                            <div style="font-size:20px;
                                                        font-weight:bold;
                                                        letter-spacing:1px;
                                                        color:#ffffff;">
                                                %s
                                            </div>
                                            <div style="margin-top:10px;
                                                        font-size:13px;
                                                        letter-spacing:1px;
                                                        color:#E2E8F0;">
                                                APPLICATION UPDATE
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="padding:36px;">
                                            <h1 style="margin:0 0 20px;
                                                       color:%s;
                                                       font-size:28px;
                                                       line-height:1.4;">
                                                Application Status Update
                                            </h1>
                                            <p style="margin:0 0 20px;
                                                      color:%s;
                                                      font-size:16px;
                                                      line-height:1.8;">
                                                Your job application status
                                                has been updated. Please check
                                                your application details.
                                            </p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="padding:20px 36px;
                                                   background:#F8FAFC;
                                                   border-top:1px solid %s;
                                                   color:%s;
                                                   font-size:13px;
                                                   line-height:1.8;">
                                            This is an automated notification
                                            from %s. Please do not reply
                                            directly to this email.
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(
                PAGE_BACKGROUND,
                PAGE_BACKGROUND,
                BORDER,
                NAVY,
                BRAND_NAME,
                TEXT_PRIMARY,
                TEXT_SECONDARY,
                BORDER,
                TEXT_SECONDARY,
                BRAND_NAME
        );
    }

    public String buildStatusChangeEmailHtml(
            ApplicationStatusChangedEvent event) {

        if (event == null) {
            return buildStatusChangeEmailHtml();
        }

        ApplicationStatus status = event.getNewStatus();

        String headerColor = getHeaderColor(status);
        String statusColor = getStatusColor(status);
        String statusBackground = getStatusBackground(status);

        String candidateName = escapeHtml(
                defaultIfBlank(event.getCandidateName(), "Candidate"));

        String jobTitle = escapeHtml(
                defaultIfBlank(event.getJobTitle(), "Job opportunity"));

        String companyName = escapeHtml(
                defaultIfBlank(event.getCompanyName(), "Hiring company"));

        String oldStatus = escapeHtml(formatStatus(event.getOldStatus()));
        String newStatus = escapeHtml(formatStatus(status));

        String statusHeadline = escapeHtml(getStatusHeadline(status));

        String applicationId = escapeHtml(
                event.getApplicationId() == null
                        ? "Not provided"
                        : String.valueOf(event.getApplicationId()));

        String noteHtml = buildNoteHtml(event.getNote());
        String changedAtHtml = buildChangedAtHtml(event.getChangedAt());

        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport"
                          content="width=device-width, initial-scale=1.0">
                    <title>Application Status Update</title>
                </head>
                <body style="margin:0;padding:0;
                             background-color:%s;
                             font-family:Arial,Helvetica,sans-serif;">
                    <table role="presentation" width="100%%"
                           cellspacing="0" cellpadding="0" border="0"
                           style="background-color:%s;padding:36px 12px;">
                        <tr>
                            <td align="center">
                                <table role="presentation" width="600"
                                       cellspacing="0" cellpadding="0"
                                       border="0"
                                       style="width:100%%;max-width:600px;
                                              background:#ffffff;
                                              border:1px solid %s;">
                                    <!-- Status-dependent header -->
                                    <tr>
                                        <td style="background-color:%s;
                                                   padding:32px;">
                                            <div style="font-size:20px;
                                                        font-weight:bold;
                                                        letter-spacing:1px;
                                                        color:#ffffff;">
                                                %s
                                            </div>
                                            <div style="margin-top:10px;
                                                        font-size:13px;
                                                        letter-spacing:1px;
                                                        color:#E2E8F0;">
                                                APPLICATION UPDATE
                                            </div>
                                        </td>
                                    </tr>
                
                                    <!-- Main content -->
                                    <tr>
                                        <td style="padding:36px;">
                                            <h1 style="margin:0 0 16px;
                                                       color:%s;
                                                       font-size:28px;
                                                       line-height:1.4;">
                                                %s
                                            </h1>
                
                                            <p style="margin:0 0 28px;
                                                      color:%s;
                                                      font-size:16px;
                                                      line-height:1.8;">
                                                Hi <strong>%s</strong>,
                                                <br><br>
                                                There has been an update to
                                                your job application. Here
                                                are the details.
                                            </p>
                
                                            <!-- Application details -->
                                            <table role="presentation"
                                                   width="100%%"
                                                   cellspacing="0"
                                                   cellpadding="0"
                                                   border="0"
                                                   style="border:1px solid %s;">
                                                <tr>
                                                    <td style="padding:24px;">
                                                        <div style="font-size:13px;
                                                                    font-weight:bold;
                                                                    letter-spacing:1px;
                                                                    color:%s;">
                                                            APPLICATION DETAILS
                                                        </div>
                
                                                        <div style="margin-top:14px;
                                                                    color:%s;
                                                                    font-size:22px;
                                                                    line-height:1.5;
                                                                    font-weight:bold;">
                                                            %s
                                                        </div>
                
                                                        <div style="margin-top:6px;
                                                                    color:%s;
                                                                    font-size:16px;
                                                                    line-height:1.6;">
                                                            %s
                                                        </div>
                
                                                        <div style="height:1px;
                                                                    background-color:%s;
                                                                    margin:24px 0;">
                                                        </div>
                
                                                        <table role="presentation"
                                                               width="100%%"
                                                               cellspacing="0"
                                                               cellpadding="0"
                                                               border="0">
                                                            <tr>
                                                                <td style="padding:10px 0;
                                                                           color:%s;
                                                                           font-size:15px;">
                                                                    Previous status
                                                                </td>
                                                                <td align="right"
                                                                    style="padding:10px 0;
                                                                           color:%s;
                                                                           font-size:15px;">
                                                                    %s
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td style="padding:10px 0;
                                                                           color:%s;
                                                                           font-size:15px;">
                                                                    Current status
                                                                </td>
                                                                <td align="right"
                                                                    style="padding:10px 0;">
                                                                    <span style="display:inline-block;
                                                                                 padding:9px 14px;
                                                                                 border-radius:20px;
                                                                                 background-color:%s;
                                                                                 color:%s;
                                                                                 font-size:13px;
                                                                                 font-weight:bold;
                                                                                 letter-spacing:0.3px;">
                                                                        %s
                                                                    </span>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td style="padding:10px 0;
                                                                           color:%s;
                                                                           font-size:15px;">
                                                                    Application ID
                                                                </td>
                                                                <td align="right"
                                                                    style="padding:10px 0;
                                                                           color:%s;
                                                                           font-size:15px;
                                                                           word-break:break-all;">
                                                                    %s
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                
                                            %s
                
                                            <p style="margin:28px 0 0;
                                                      color:%s;
                                                      font-size:16px;
                                                      line-height:1.8;">
                                                Thank you for using %s.
                                                We wish you the best in your
                                                career journey.
                                            </p>
                                        </td>
                                    </tr>
                
                                    <!-- Footer -->
                                    <tr>
                                        <td style="padding:24px 36px;
                                                   background:#F8FAFC;
                                                   border-top:1px solid %s;
                                                   color:%s;
                                                   font-size:13px;
                                                   line-height:1.8;">
                                            This is an automated notification
                                            from %s. Please do not reply
                                            directly to this email.
                                            %s
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(
                PAGE_BACKGROUND,
                PAGE_BACKGROUND,
                BORDER,
                headerColor,
                BRAND_NAME,
                TEXT_PRIMARY,
                statusHeadline,
                TEXT_SECONDARY,
                candidateName,
                BORDER,
                TEXT_SECONDARY,
                TEXT_PRIMARY,
                jobTitle,
                TEXT_SECONDARY,
                companyName,
                BORDER,
                TEXT_SECONDARY,
                TEXT_SECONDARY,
                oldStatus,
                TEXT_SECONDARY,
                statusBackground,
                statusColor,
                newStatus,
                TEXT_SECONDARY,
                TEXT_SECONDARY,
                applicationId,
                noteHtml,
                TEXT_SECONDARY,
                BRAND_NAME,
                BORDER,
                TEXT_SECONDARY,
                BRAND_NAME,
                changedAtHtml
        );
    }

    private String getHeaderColor(ApplicationStatus status) {
        if (status == null) {
            return NAVY;
        }

        return switch (status) {
            case SHORTLISTED -> "#166534";
            case INTERVIEW_SCHEDULED -> "#1D4ED8";
            case HIRED -> "#047857";
            case REJECTED -> "#334155";
            case PENDING -> "#92400E";
            case WITHDRAWN -> "#475569";
            default -> NAVY;
        };
    }

    private String getStatusColor(ApplicationStatus status) {
        if (status == null) {
            return "#475569";
        }

        return switch (status) {
            case SHORTLISTED -> "#166534";
            case REJECTED -> "#991B1B";
            case PENDING -> "#92400E";
            case INTERVIEW_SCHEDULED -> "#1D4ED8";
            case HIRED -> "#047857";
            case WITHDRAWN -> "#475569";
            default -> "#334155";
        };
    }

    private String getStatusBackground(ApplicationStatus status) {
        if (status == null) {
            return "#F1F5F9";
        }

        return switch (status) {
            case SHORTLISTED -> "#DCFCE7";
            case REJECTED -> "#FEE2E2";
            case PENDING -> "#FEF3C7";
            case INTERVIEW_SCHEDULED -> "#DBEAFE";
            case HIRED -> "#D1FAE5";
            case WITHDRAWN -> "#E2E8F0";
            default -> "#F1F5F9";
        };
    }

    private String getStatusHeadline(ApplicationStatus status) {
        if (status == null) {
            return "Application Status Update";
        }

        return switch (status) {
            case SHORTLISTED -> "Great news!";
            case INTERVIEW_SCHEDULED -> "Your next step awaits";
            case HIRED -> "Congratulations!";
            case REJECTED -> "Application Update";
            case PENDING -> "Application Under Review";
            case WITHDRAWN -> "Application Update";
            default -> "Application Status Update";
        };
    }

    private String formatStatus(ApplicationStatus status) {
        if (status == null) {
            return "Not available";
        }

        String formatted = status.name().replace('_', ' ').toLowerCase();
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char ch : formatted.toCharArray()) {
            if (Character.isWhitespace(ch)) {
                result.append(ch);
                capitalizeNext = true;
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(ch));
                capitalizeNext = false;
            } else {
                result.append(ch);
            }
        }

        return result.toString();
    }

    private String buildNoteHtml(String note) {
        if (note == null || note.isBlank()) {
            return "";
        }

        return """
                <table role="presentation" width="100%%"
                       cellspacing="0" cellpadding="0" border="0"
                       style="margin-top:28px;background:#F8FAFC;
                              border-left:4px solid %s;">
                    <tr>
                        <td style="padding:20px;">
                            <div style="font-size:14px;
                                        font-weight:bold;
                                        color:%s;
                                        margin-bottom:10px;">
                                MESSAGE FROM THE HIRING TEAM
                            </div>
                            <div style="font-size:16px;
                                        line-height:1.8;
                                        color:%s;
                                        white-space:pre-line;">
                                %s
                            </div>
                        </td>
                    </tr>
                </table>
                """.formatted(
                BORDER,
                TEXT_PRIMARY,
                TEXT_SECONDARY,
                escapeHtml(note)
        );
    }

    private String buildChangedAtHtml(Object changedAt) {
        if (changedAt == null) {
            return "";
        }

        String formattedDate = changedAt.toString();

        if (changedAt instanceof TemporalAccessor temporal) {
            try {
                formattedDate = DateTimeFormatter
                        .ofPattern("dd MMM yyyy, hh:mm a")
                        .format(temporal);
            } catch (RuntimeException ignored) {
                // Retain the original value if the date cannot be formatted.
            }
        }

        return """
                <div style="margin-top:10px;
                            font-size:13px;
                            color:%s;">
                    Updated: %s
                </div>
                """.formatted(
                TEXT_SECONDARY,
                escapeHtml(formattedDate)
        );
    }

    private String defaultIfBlank(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
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