package com.zebra.plugins.rfid;

import android.content.Context;
import android.util.SparseIntArray;

import com.zebra.rfid.api3.RFIDResults;
import com.zebra.plugins.rfid.R;

public class ErrorUtils {
    private static final SparseIntArray errorMessages = new SparseIntArray();

    static {
        errorMessages.put(RFIDResults.RFID_API_SUCCESS.ordinal, R.string.rfid_api_success);
        errorMessages.put(RFIDResults.RFID_API_COMMAND_TIMEOUT.ordinal, R.string.rfid_api_command_timeout);
        errorMessages.put(RFIDResults.RFID_API_PARAM_ERROR.ordinal, R.string.rfid_api_param_error);
        errorMessages.put(RFIDResults.RFID_API_PARAM_OUT_OF_RANGE.ordinal, R.string.rfid_api_param_out_of_range);
        errorMessages.put(RFIDResults.RFID_API_CANNOT_ALLOC_MEM.ordinal, R.string.rfid_api_cannot_alloc_mem);
        errorMessages.put(RFIDResults.RFID_API_UNKNOWN_ERROR.ordinal, R.string.rfid_api_unknown_error);
        errorMessages.put(RFIDResults.RFID_API_INVALID_HANDLE.ordinal, R.string.rfid_api_invalid_handle);
        errorMessages.put(RFIDResults.RFID_API_BUFFER_TOO_SMALL.ordinal, R.string.rfid_api_buffer_too_small);
        errorMessages.put(RFIDResults.RFID_READER_FUNCTION_UNSUPPORTED.ordinal, R.string.rfid_reader_function_unsupported);
        errorMessages.put(RFIDResults.RFID_RECONNECT_FAILED.ordinal, R.string.rfid_reconnect_failed);
        errorMessages.put(RFIDResults.RFID_API_DATA_NOT_INITIALISED.ordinal, R.string.rfid_api_data_not_initialised);
        errorMessages.put(RFIDResults.RFID_API_ZONE_ID_ALREADY_EXITS.ordinal, R.string.rfid_api_zone_id_already_exits);
        errorMessages.put(RFIDResults.RFID_API_ZONE_ID_NOT_FOUND.ordinal, R.string.rfid_api_zone_id_not_found);
        errorMessages.put(RFIDResults.RFID_COMM_OPEN_ERROR.ordinal, R.string.rfid_comm_open_error);
        errorMessages.put(RFIDResults.RFID_COMM_CONNECTION_ALREADY_EXISTS.ordinal, R.string.rfid_comm_connection_already_exists);
        errorMessages.put(RFIDResults.RFID_COMM_RESOLVE_ERROR.ordinal, R.string.rfid_comm_resolve_error);
        errorMessages.put(RFIDResults.RFID_COMM_SEND_ERROR.ordinal, R.string.rfid_comm_send_error);
        errorMessages.put(RFIDResults.RFID_COMM_RECV_ERROR.ordinal, R.string.rfid_comm_recv_error);
        errorMessages.put(RFIDResults.RFID_COMM_NO_CONNECTION.ordinal, R.string.rfid_comm_no_connection);
        errorMessages.put(RFIDResults.RFID_INVALID_SOCKET.ordinal, R.string.rfid_invalid_socket);
        errorMessages.put(RFIDResults.RFID_READER_REGION_NOT_CONFIGURED.ordinal, R.string.rfid_reader_region_not_configured);
        errorMessages.put(RFIDResults.RFID_READER_REINITIALIZING.ordinal, R.string.rfid_reader_reinitializing);
        errorMessages.put(RFIDResults.RFID_SECURE_CONNECTION_ERROR.ordinal, R.string.rfid_secure_connection_error);
        errorMessages.put(RFIDResults.RFID_ROOT_SECURITY_CERTIFICATE_ERROR.ordinal, R.string.rfid_root_security_certificate_error);
        errorMessages.put(RFIDResults.RFID_HOST_SECURITY_CERTIFICATE_ERROR.ordinal, R.string.rfid_host_security_certificate_error);
        errorMessages.put(RFIDResults.RFID_HOST_SECURITY_KEY_ERROR.ordinal, R.string.rfid_host_security_key_error);
        errorMessages.put(RFIDResults.RFID_CONNECTION_PASSWORD_ERROR.ordinal, R.string.rfid_connection_password_error);
        errorMessages.put(RFIDResults.RFID_CONFIG_GET_FAILED.ordinal, R.string.rfid_config_get_failed);
        errorMessages.put(RFIDResults.RFID_CONFIG_SET_FAILED.ordinal, R.string.rfid_config_set_failed);
        errorMessages.put(RFIDResults.RFID_CAP_NOT_SUPPORTED.ordinal, R.string.rfid_cap_not_supported);
        errorMessages.put(RFIDResults.RFID_CAP_GET_FAILED.ordinal, R.string.rfid_cap_get_failed);
        errorMessages.put(RFIDResults.RFID_FILTER_NO_FILTER.ordinal, R.string.rfid_filter_no_filter);
        errorMessages.put(RFIDResults.RFID_FILTER_INVALID_INDEX.ordinal, R.string.rfid_filter_invalid_index);
        errorMessages.put(RFIDResults.RFID_FILTER_MAX_FILTERS_EXCEEDED.ordinal, R.string.rfid_filter_max_filters_exceeded);
        errorMessages.put(RFIDResults.RFID_NO_READ_TAGS.ordinal, R.string.rfid_no_read_tags);
        errorMessages.put(RFIDResults.RFID_NO_REPORTED_EVENTS.ordinal, R.string.rfid_no_reported_events);
        errorMessages.put(RFIDResults.RFID_INVENTORY_MAX_TAGS_EXCEEDED.ordinal, R.string.rfid_inventory_max_tags_exceeded);
        errorMessages.put(RFIDResults.RFID_INVENTORY_IN_PROGRESS.ordinal, R.string.rfid_inventory_in_progress);
        errorMessages.put(RFIDResults.RFID_NO_INVENTORY_IN_PROGRESS.ordinal, R.string.rfid_no_inventory_in_progress);
        errorMessages.put(RFIDResults.RFID_TAG_LOCATING_IN_PROGRESS.ordinal, R.string.rfid_tag_locating_in_progress);
        errorMessages.put(RFIDResults.RFID_NO_TAG_LOCATING_IN_PROGRESS.ordinal, R.string.rfid_no_tag_locating_in_progress);
        errorMessages.put(RFIDResults.RFID_NXP_EAS_SCAN_IN_PROGRESS.ordinal, R.string.rfid_nxp_eas_scan_in_progress);
        errorMessages.put(RFIDResults.RFID_NO_NXP_EAS_SCAN_IN_PROGRESS.ordinal, R.string.rfid_no_nxp_eas_scan_in_progress);
        errorMessages.put(RFIDResults.RFID_BATCHMODE_IN_PROGRESS.ordinal, R.string.rfid_batchmode_in_progress);
        errorMessages.put(RFIDResults.RFID_ACCESS_DPO_ENABLED_ERROR.ordinal, R.string.rfid_access_dpo_enabled_error);
        errorMessages.put(RFIDResults.RFID_OPERATION_IN_PROGRESS.ordinal, R.string.rfid_operation_in_progress);
        errorMessages.put(RFIDResults.RFID_CHARGING_COMMAND_NOT_ALLOWED.ordinal, R.string.rfid_charging_command_not_allowed);
        errorMessages.put(RFIDResults.RFID_ACCESS_IN_PROGRESS.ordinal, R.string.rfid_access_in_progress);
        errorMessages.put(RFIDResults.RFID_NO_ACCESS_IN_PROGRESS.ordinal, R.string.rfid_no_access_in_progress);
        errorMessages.put(RFIDResults.RFID_ACCESS_TAG_READ_FAILED.ordinal, R.string.rfid_access_tag_read_failed);
        errorMessages.put(RFIDResults.RFID_ACCESS_TAG_WRITE_FAILED.ordinal, R.string.rfid_access_tag_write_failed);
        errorMessages.put(RFIDResults.RFID_ACCESS_TAG_LOCK_FAILED.ordinal, R.string.rfid_access_tag_lock_failed);
        errorMessages.put(RFIDResults.RFID_ACCESS_TAG_KILL_FAILED.ordinal, R.string.rfid_access_tag_kill_failed);
        errorMessages.put(RFIDResults.RFID_ACCESS_TAG_BLOCK_ERASE_FAILED.ordinal, R.string.rfid_access_tag_block_erase_failed);
        errorMessages.put(RFIDResults.RFID_ACCESS_TAG_BLOCK_WRITE_FAILED.ordinal, R.string.rfid_access_tag_block_write_failed);
        errorMessages.put(RFIDResults.RFID_ACCESS_TAG_NOT_FOUND.ordinal, R.string.rfid_access_tag_not_found);
        errorMessages.put(RFIDResults.RFID_ACCESS_SEQUENCE_NOT_INITIALIZED.ordinal, R.string.rfid_access_sequence_not_initialized);
        errorMessages.put(RFIDResults.RFID_ACCESS_SEQUENCE_EMPTY.ordinal, R.string.rfid_access_sequence_empty);
        errorMessages.put(RFIDResults.RFID_ACCESS_SEQUENCE_IN_USE.ordinal, R.string.rfid_access_sequence_in_use);
        errorMessages.put(RFIDResults.RFID_ACCESS_SEQUENCE_MAX_OP_EXCEEDED.ordinal, R.string.rfid_access_sequence_max_op_exceeded);
        errorMessages.put(RFIDResults.RFID_ACCESS_TAG_RECOMMISSION_FAILED.ordinal, R.string.rfid_access_tag_recommission_failed);
        errorMessages.put(RFIDResults.RFID_ACCESS_TAG_BLOCK_PERMALOCK_FAILED.ordinal, R.string.rfid_access_tag_block_permalock_failed);
        errorMessages.put(RFIDResults.RFID_ACCESS_NXP_TAG_SET_EAS_FAILED.ordinal, R.string.rfid_access_nxp_tag_set_eas_failed);
        errorMessages.put(RFIDResults.RFID_ACCESS_NXP_TAG_READ_PROTECT_FAILED.ordinal, R.string.rfid_access_nxp_tag_read_protect_failed);
        errorMessages.put(RFIDResults.RFID_ACCESS_SEQUENCE_ADDITION_FAILED.ordinal, R.string.rfid_access_sequence_addition_failed);
        errorMessages.put(RFIDResults.RFID_ACCESS_NXP_CHANGE_CONFIG_FAILED.ordinal, R.string.rfid_access_nxp_change_config_failed);
        errorMessages.put(RFIDResults.RFID_ACCESS_IMPINJ_QT_READ_FAILED.ordinal, R.string.rfid_access_impinj_qt_read_failed);
        errorMessages.put(RFIDResults.RFID_ACCESS_IMPINJ_QT_WRITE_FAILED.ordinal, R.string.rfid_access_impinj_qt_write_failed);
        errorMessages.put(RFIDResults.RFID_RM_INVALID_USERNAME_PASSWORD.ordinal, R.string.rfid_rm_invalid_username_password);
        errorMessages.put(RFIDResults.RFID_RM_NO_UPDATION_IN_PROGRESS.ordinal, R.string.rfid_rm_no_updation_in_progress);
        errorMessages.put(RFIDResults.RFID_RM_UPDATION_IN_PROGRESS.ordinal, R.string.rfid_rm_updation_in_progress);
        errorMessages.put(RFIDResults.RFID_RM_COMMAND_FAILED.ordinal, R.string.rfid_rm_command_failed);
        errorMessages.put(RFIDResults.RFID_INVALID_ERROR_CODE.ordinal, R.string.rfid_invalid_error_code);
        errorMessages.put(RFIDResults.RFID_API_LOCK_ACQUIRE_FAILURE.ordinal, R.string.rfid_api_lock_acquire_failure);
        errorMessages.put(RFIDResults.RFID_API_OPTION_NOT_ALLOWED.ordinal, R.string.rfid_api_option_not_allowed);
        errorMessages.put(RFIDResults.RFID_COMMAND_OPTION_WITHOUT_DELIMITER.ordinal, R.string.rfid_command_option_without_delimiter);
        errorMessages.put(RFIDResults.RFID_MAX_CHARS_EXCEEDED.ordinal, R.string.rfid_max_chars_exceeded);
    }

    public static String getErrorMessage(Context context, int errorCode) {
        return context.getString(errorMessages.get(errorCode, R.string.error_unknown));
    }
}
