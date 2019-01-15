package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;


/**
 * Created by Android on 12/23/2016.
 */

public class Members {


    public int getSecond_marriage_reason_id() {
        return second_marriage_reason_id;
    }

    public void setSecond_marriage_reason_id(int second_marriage_reason_id) {
        this.second_marriage_reason_id = second_marriage_reason_id;
    }

    public String getAbout_my_choice() {
        return about_my_choice;
    }

    public void setAbout_my_choice(String about_my_choice) {
        this.about_my_choice = about_my_choice;
    }

    @SerializedName("second_marriage_reason_id")
    private int second_marriage_reason_id;
    @SerializedName("about_my_choice")
    private String about_my_choice;

    @SerializedName("alias")
    public String alias;
    @SerializedName("id")
    private long _id;
    @SerializedName("self")
    private long _self;

    @SerializedName("count")
    private long _count;

    @SerializedName("isamfeatured")
    private long _isamfeatured;

    @SerializedName("interest_ids")
    private String _interest_ids;

    @SerializedName("issubscribed")
    private long _issubscribed;

    @SerializedName("name")
    private String _name;

    @SerializedName("cellno")
    private String _cellno;

    @SerializedName("birthdate")
    private String _birthdate;

    @SerializedName("progressbar")
    private long _progressbar;

    @SerializedName("details")
    private String _details;

    @SerializedName("memberid")
    private long _memberid;

    @SerializedName("visting_memberid")
    private long _visting_memberid;

    @SerializedName("profile_owner_id")
    private long _profile_owner_id;

    @SerializedName("matched_member_count")
    private String _matched_member_count;

    @SerializedName("path")
    private String _path;

    @SerializedName("directpath")
    private String _directpath;

    @SerializedName("profile_owner")
    private String _profile_owner;

    @SerializedName("personal_name")
    private String _personal_name;

    @SerializedName("gender")
    private String _gender;

    @SerializedName("country_id")
    private long _country_id;

    @SerializedName("country_currency")
    private String _country_currency;

    @SerializedName("member_country_name")
    private String _member_country_name;

    @SerializedName("email")
    private String _email;

    @SerializedName("password")
    private String _password;

    @SerializedName("new_password")
    private String _new_password;

    @SerializedName("admin_notes")
    private String _admin_notes;

    @SerializedName("member_source")
    private long _member_source;

    @SerializedName("member_status")
    private long _member_status;

    @SerializedName("member_ip")
    private String _member_ip;

    @SerializedName("rememberme")
    private boolean _rememberme;

    @SerializedName("visa_status_id")
    private int _visa_status_id;


    @SerializedName("state_id")
    private long _state_id;

    @SerializedName("city_id")
    private long _city_id;

    @SerializedName("postal_code")
    private String _postal_code;

    @SerializedName("choice_country_ids")
    private String _choice_country_ids;

    @SerializedName("choice_state_ids")
    private String _choice_state_ids;

    @SerializedName("choice_cities_ids")
    private String _choice_cities_ids;

    @SerializedName("choice_caste_ids")
    private String _choice_caste_ids;

    @SerializedName("choice_income_ids")
    private String _choice_income_ids;

    @SerializedName("choice_visa_status_ids")
    private String _choice_visa_status_ids;

    @SerializedName("body_id")
    private int _body_id;

    @SerializedName("hair_color_id")
    private int _hair_color_id;

    @SerializedName("eye_color_id")
    private int _eye_color_id;

    @SerializedName("complexion_id")
    private int _complexion_id;

    @SerializedName("height_id")
    private long _height_id;

    @SerializedName("choice_age_from")
    private long _choice_age_from;

    @SerializedName("choice_age_upto")
    private long _choice_age_upto;

    @SerializedName("choice_body_ids")
    private String _choice_body_ids;

    @SerializedName("choice_eye_color_ids")
    private String _choice_eye_color_ids;

    @SerializedName("choice_hair_color_ids")
    private String _choice_hair_color_ids;

    @SerializedName("choice_complexion_ids")
    private String _choice_complexion_ids;

    @SerializedName("choice_height_from_id")
    private long _choice_height_from_id;

    @SerializedName("choice_height_to_id")
    private long _choice_height_to_id;

    @SerializedName("choice_preferences_ids")
    private String _choice_preferences_ids;

    @SerializedName("choice_preferences")
    private String _choice_preferences;

    @SerializedName("education_id")
    private long _education_id;

    @SerializedName("education_field_id")
    private long _education_field_id;

    @SerializedName("economy_id")
    private int _economy_id;
    @SerializedName("occupation_id")
    private long _occupation_id;
    @SerializedName("income_level_id")
    private long _income_level_id;
    @SerializedName("income_level_unit")
    private String _income_level_unit;
    @SerializedName("income_level_type")
    private String _income_level_type;
    @SerializedName("religion_id")
    private long _religion_id;
    @SerializedName("religious_sect_id")
    private int _religious_sect_id;
    @SerializedName("religious_sec_type")
    private String religious_sec_type;
    @SerializedName("ethnic_background_id")
    private int _ethnic_background_id;
    @SerializedName("caste_id")
    private long _caste_id;
    @SerializedName("sibling_id")
    private long _sibling_id;
    @SerializedName("brothers_count")
    private String _brothers_count;
    @SerializedName("sisters_count")
    private String _sisters_count;
    @SerializedName("marital_status_id")
    private int _marital_status_id;
    @SerializedName("children_id")
    private int _children_id;
    @SerializedName("living_arrangement_id")
    private int _living_arrangement_id;
    @SerializedName("family_values_id")
    private int _family_values_id;
    @SerializedName("hijab_id")
    private int _hijab_id;
    @SerializedName("raised_id")
    private int _raised_id;
    @SerializedName("smoking_id")
    private int _smoking_id;
    @SerializedName("drink_id")
    private int _drink_id;
    @SerializedName("choice_education_ids")
    private String _choice_education_ids;
    @SerializedName("choice_occupation_ids")

    private String _choice_occupation_ids;
    @SerializedName("choice_economy_ids")
    private String _choice_economy_ids;
    @SerializedName("choice_religious_sect_ids")
    private String _choice_religious_sect_ids;
    @SerializedName("choice_ethnic_bground_ids")
    private String _choice_ethnic_bground_ids;
    @SerializedName("choice_sibling_ids")
    private String _choice_sibling_ids;
    @SerializedName("choice_marital_status_ids")
    private String _choice_marital_status_ids;
    @SerializedName("choice_children_ids")
    private String _choice_children_ids;
    @SerializedName("choice_living_arangment_ids")
    private String _choice_living_arangment_ids;
    @SerializedName("choice_family_values_ids")
    private String _choice_family_values_ids;
    @SerializedName("choice_hijab_ids")
    private String _choice_hijab_ids;
    @SerializedName("choice_raised_ids")
    private String _choice_raised_ids;
    @SerializedName("choice_smoking_ids")
    private String _choice_smoking_ids;
    @SerializedName("choice_drink_ids")
    private String _choice_drink_ids;
    @SerializedName("type")
    private String _type;
    @SerializedName("my_id")
    private long _my_id;
    @SerializedName("choice_ids")
    private String _choice_ids;
    @SerializedName("mychecked")
    private boolean _mychecked;
    @SerializedName("choicechecked")
    private boolean _choicechecked;
    @SerializedName("about_type_id")
    private long _about_type_id;
    @SerializedName("about_member_id")
    private long _about_member_id;
    @SerializedName("about_type")
    private String _about_type;
    @SerializedName("about_member")
    private String _about_member;
    @SerializedName("photo_id")
    private long _photo_id;
    @SerializedName("photo_name")
    private String _photo_name;
    @SerializedName("photo_ext")
    private String _photo_ext;
    @SerializedName("photo_server_name")
    private String _photo_server_name;
    @SerializedName("photo_file_size")
    private long _photo_file_size;
    @SerializedName("photo_status")
    private long _photo_status;
    @SerializedName("photo_create_date")
    private String _photo_create_date;
    @SerializedName("joined_date")
    private String _joined_date;
    @SerializedName("photo_path")
    private String _photo_path;
    @SerializedName("photo_path_thb")
    private String _photo_path_thb;
    @SerializedName("age")
    private String _age;
    @SerializedName("country_name")
    private String _country_name;
    @SerializedName("country_flag")
    private String _country_flag;
    @SerializedName("country_flag1")
    private String _country_flag1;
    @SerializedName("state_name")
    private String _state_name;
    @SerializedName("city_name")
    private String _city_name;
    @SerializedName("visa_status_types")
    private String _visa_status_types;
    @SerializedName("photo_count")
    private long _photo_count;
    @SerializedName("body_types")
    private String _body_types;
    @SerializedName("hair_color_types")
    private String _hair_color_types;
    @SerializedName("eye_color_types")
    private String _eye_color_types;
    @SerializedName("complexion_types")
    private String _complexion_types;
    @SerializedName("height_description")
    private String _height_description;
    @SerializedName("education_types")
    private String _education_types;
    @SerializedName("education_field_types")
    private String _education_field_types;
    @SerializedName("occupation_types")
    private String _occupation_types;
    @SerializedName("income_level")
    private String _income_level;
    @SerializedName("sibling_types")
    private String _sibling_types;
    @SerializedName("caste_name")
    private String _caste_name;
    @SerializedName("marital_status_types")
    private String _marital_status_types;
    @SerializedName("children_types")
    private String _children_types;
    @SerializedName("living_arrabgements_types")
    private String _living_arrabgements_types;
    @SerializedName("hijab_types")
    private String _hijab_types;
    @SerializedName("raised_types")
    private String _raised_types;
    @SerializedName("family_values_types")
    private String _family_values_types;
    @SerializedName("smoking_types")
    private String _smoking_types;
    @SerializedName("registration_date")
    private String _registration_date;
    @SerializedName("ethnic_background_types")
    private String _ethnic_background_types;
    @SerializedName("drinks_types")
    private String _drinks_types;
    @SerializedName("boys_count")
    private long _boys_count;
    @SerializedName("girls_count")
    private long _girls_count;
    @SerializedName("min_age")
    private long _min_age;
    @SerializedName("max_age")
    private long _max_age;
    @SerializedName("registration_within_id")
    private long _registration_within_id;
    @SerializedName("pictureonly")
    private int _pictureonly;
    @SerializedName("opentopublic")
    private int _opentopublic;
    @SerializedName("choice_profile_owner_Ids")
    private String _choice_profile_owner_Ids;
    @SerializedName("last_login_date_id")
    private long _last_login_date_id;
    @SerializedName("choice_zodiac_sign_ids")
    private String _choice_zodiac_sign_ids;
    @SerializedName("choice_height_from")
    private String _choice_height_from;
    @SerializedName("choice_height_to")
    private String _choice_height_to;
    @SerializedName("choice_country_names")
    private String _choice_country_names;
    @SerializedName("choice_visa_status")
    private String _choice_visa_status;
    @SerializedName("choice_body")
    private String _choice_body;
    @SerializedName("choice_hair_color")
    private String _choice_hair_color;
    @SerializedName("choice_eye_color")
    private String _choice_eye_color;
    @SerializedName("choice_complexion")
    private String _choice_complexion;
    @SerializedName("choice_education")
    private String _choice_education;
    @SerializedName("choice_occupation")
    private String _choice_occupation;
    @SerializedName("choice_religious_sec")
    private String _choice_religious_sec;
    @SerializedName("choice_marital_status")
    private String _choice_marital_status;
    @SerializedName("choice_children")
    private String _choice_children;
    @SerializedName("choice_living_arrangements")
    private String _choice_living_arrangements;
    @SerializedName("choice_family_values")
    private String _choice_family_values;
    @SerializedName("choice_hijab")
    private String _choice_hijab;
    @SerializedName("choice_raised")
    private String _choice_raised;
    @SerializedName("choice_smoking")
    private String _choice_smoking;
    @SerializedName("choice_ethnic_background")
    private String _choice_ethnic_background;
    @SerializedName("choice_drinks")
    private String _choice_drinks;
    @SerializedName("reviewed")
    private long _reviewed;
    @SerializedName("created_date")
    private String _created_date;
    @SerializedName("personality_ids")
    private String _personality_ids;
    @SerializedName("total_member_count")
    private long _total_member_count;
    @SerializedName("total_pages")
    private int _total_pages;
    @SerializedName("page_no")
    private long _page_no;
    @SerializedName("viewd")
    private long _viewd;
    @SerializedName("hidden_status")
    private long _hidden_status;
    @SerializedName("sign_name")
    private String _sign_name;
    @SerializedName("default_image")
    private String _default_image;
    @SerializedName("request_response_id")
    private long _request_response_id;
    @SerializedName("email_verified")
    private long _email_verified;
    @SerializedName("phone_verified")
    private long _phone_verified;
    @SerializedName("hide_phone")
    private long _hide_phone;
    @SerializedName("phone_request_response_id")
    private long _phone_request_response_id;
    @SerializedName("phone_request_id")
    private long _phone_request_id;
    @SerializedName("phone_privilege_id")
    private long _phone_privilege_id;
    @SerializedName("phone_home")
    private String _phone_home;
    @SerializedName("phone_office")
    private long _phone_office;

    @SerializedName("phone_mobile")
    private String _phone_mobile;
    @SerializedName("photo_request_id")
    private long _photo_request_id;
    @SerializedName("photo_privilege_id")
    private long _photo_privilege_id;
    @SerializedName("profile_request_id")
    private long _profile_request_id;
    @SerializedName("photo_upload_request_id")
    private long _photo_upload_request_id;
    @SerializedName("featured_profile")
    private long _featured_profile;
    @SerializedName("accept_message")
    private long _accept_message;
    @SerializedName("open_message")
    private long _open_message;
    @SerializedName("notes")
    private String _notes;
    @SerializedName("member_notes_id")
    private long _member_notes_id;
    @SerializedName("deactivate_reason")
    private String _deactivate_reason;
    @SerializedName("deactivate_reason_id")
    private long _deactivate_reason_id;
    @SerializedName("no_data")
    private long _no_data;
    @SerializedName("allow")
    private long _allow;
    @SerializedName("other_info")
    private String _other_info;
    @SerializedName("good_quality")
    private String _good_quality;
    @SerializedName("most_thankfull")
    private String _most_thankfull;
    @SerializedName("for_fun")
    private String _for_fun;
    @SerializedName("last_login_date")
    private String _last_login_date;
    @SerializedName("blocked_member")
    private long _blocked_member;
    @SerializedName("removed_member")
    private long _removed_member;
    @SerializedName("saved_member")
    private long _saved_member;
    @SerializedName("hide_photo")
    private long _hide_photo;
    @SerializedName("hide_profile")
    private long _hide_profile;
    @SerializedName("image_privilege_id")
    private long _image_privilege_id;
    @SerializedName("profile_privilege_id")
    private long _profile_privilege_id;
    @SerializedName("request_image")
    private long _request_image;
    @SerializedName("request_image_view")
    private long _request_image_view;
    @SerializedName("request_profile_view")
    private long _request_profile_view;
    @SerializedName("request_phone_view")
    private long _request_phone_view;
    @SerializedName("request_id")
    private long _request_id;
    @SerializedName("interest_response")
    private long _interest_response;
    @SerializedName("interested_id")
    private long _interested_id = 0;
    @SerializedName("interest_received")
    private long _interest_received;
    @SerializedName("image_count")
    private long _image_count;
    @SerializedName("allow_phone_view")
    private long _allow_phone_view;
    @SerializedName("cond_photo_privilege")
    private long _cond_photo_privilege;
    @SerializedName("cond_phone_privilege")
    private long _cond_phone_privilege;
    @SerializedName("image_view")
    private long _image_view;
    @SerializedName("phone_view")
    private long _phone_view;
    @SerializedName("pref1")
    private String _pref1;
    @SerializedName("pref2")
    private String _pref2;
    @SerializedName("pref3")
    private String _pref3;
    @SerializedName("pref4")
    private String _pref4;
    @SerializedName("prefvalue1")
    private String _prefvalue1;
    @SerializedName("prefvalue2")
    private String _prefvalue2;
    @SerializedName("prefvalue3")
    private String _prefvalue3;
    @SerializedName("prefvalue4")
    private String _prefvalue4;
    @SerializedName("to_member_id_dec")
    private String _to_member_id_dec;
    @SerializedName("start_date")
    private String _start_date;
    @SerializedName("end_date")
    private String _end_date;
    @SerializedName("blur")
    private long _blur;
    @SerializedName("isedit")
    private long _isedit;

    @SerializedName("userpath")
    private String userpath;


    @SerializedName("privilege_type_id")
    private long _privilege_type_id;

    @SerializedName("physic_id")
    private int physic_id;

    @SerializedName("choice_physic_ids")
    private String choice_physic_ids;

    @SerializedName("physics_types")
    private String physics_types;

    public int getPhysic_id() {
        return physic_id;
    }

    public void setPhysic_id(int physic_id) {
        this.physic_id = physic_id;
    }

    public String getChoice_physic_ids() {
        return choice_physic_ids;
    }

    public void setChoice_physic_ids(String choice_physic_ids) {
        this.choice_physic_ids = choice_physic_ids;
    }

    public String getPhysics_types() {
        return physics_types;
    }

    public void setPhysics_types(String physics_types) {
        this.physics_types = physics_types;
    }

    public String getChoice_physics() {
        return choice_physics;
    }

    public void setChoice_physics(String choice_physics) {
        this.choice_physics = choice_physics;
    }

    @SerializedName("choice_physics")

    private String choice_physics;


    public void setUserpath(String userpath) {
        this.userpath = userpath;
    }

    public String getUserpath() {
        return userpath;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long get_self() {
        return _self;
    }

    public void set_self(long _self) {
        this._self = _self;
    }

    public long get_count() {
        return _count;
    }

    public void set_count(long _count) {
        this._count = _count;
    }

    public long get_isamfeatured() {
        return _isamfeatured;
    }

    public void set_isamfeatured(long _isamfeatured) {
        this._isamfeatured = _isamfeatured;
    }

    public void setInterest_ids(String interest_ids) {
        this._interest_ids = interest_ids;
    }

    public String get_interest_ids() {
        return _interest_ids;
    }

    public void set_interest_ids(String _interest_ids) {
        this._interest_ids = _interest_ids;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_cellno() {
        return _cellno;
    }

    public void set_cellno(String _cellno) {
        this._cellno = _cellno;
    }

    public String get_birthdate() {
        return _birthdate;
    }

    public void set_birthdate(String _birthdate) {
        this._birthdate = _birthdate;
    }

    public long get_progressbar() {
        return _progressbar;
    }

    public void set_progressbar(long _progressbar) {
        this._progressbar = _progressbar;
    }

    public String get_details() {
        return _details;
    }

    public void set_details(String _details) {
        this._details = _details;
    }

    public long get_memberid() {
        return _memberid;
    }

    public void set_memberid(long _memberid) {
        this._memberid = _memberid;
    }

    public int get_visa_status_id() {
        return _visa_status_id;
    }

    public void set_visa_status_id(int _visa_status_id) {
        this._visa_status_id = _visa_status_id;
    }

    public long get_profile_owner_id() {
        return _profile_owner_id;
    }

    public void set_profile_owner_id(long _profile_owner_id) {
        this._profile_owner_id = _profile_owner_id;
    }

    public String get_matched_member_count() {
        return _matched_member_count;
    }

    public void set_matched_member_count(String _matched_member_count) {
        this._matched_member_count = _matched_member_count;
    }

    public String get_path() {
        return _path;
    }

    public void set_path(String _path) {
        this._path = _path;
    }

    public String get_directpath() {
        return _directpath;
    }

    public void set_directpath(String _directpath) {
        this._directpath = _directpath;
    }

    public String get_profile_owner() {
        return _profile_owner;
    }

    public void set_profile_owner(String _profile_owner) {
        this._profile_owner = _profile_owner;
    }

    public String get_personal_name() {
        return _personal_name;
    }

    public void set_personal_name(String _personal_name) {
        this._personal_name = _personal_name;
    }

    public String get_gender() {
        return _gender;
    }

    public void set_gender(String _gender) {
        this._gender = _gender;
    }

    public long get_country_id() {
        return _country_id;
    }

    public void set_country_id(long _country_id) {
        this._country_id = _country_id;
    }

    public String get_country_currency() {
        return _country_currency;
    }

    public void set_country_currency(String _country_currency) {
        this._country_currency = _country_currency;
    }

    public String get_member_country_name() {
        return _member_country_name;
    }

    public void set_member_country_name(String _member_country_name) {
        this._member_country_name = _member_country_name;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public String get_new_password() {
        return _new_password;
    }

    public void set_new_password(String _new_password) {
        this._new_password = _new_password;
    }

    public String get_admin_notes() {
        return _admin_notes;
    }

    public void set_admin_notes(String _admin_notes) {
        this._admin_notes = _admin_notes;
    }

    public long get_member_source() {
        return _member_source;
    }

    public void set_member_source(long _member_source) {
        this._member_source = _member_source;
    }

    public long get_member_status() {
        return _member_status;
    }

    public void set_member_status(long _member_status) {
        this._member_status = _member_status;
    }

    public String get_member_ip() {
        return _member_ip;
    }

    public void set_member_ip(String _member_ip) {
        this._member_ip = _member_ip;
    }

    public boolean get_rememberme() {
        return _rememberme;
    }

    public String get_choice_visa_status_ids() {
        return _choice_visa_status_ids;
    }

    public void set_choice_visa_status_ids(String _choice_visa_status_ids) {
        this._choice_visa_status_ids = _choice_visa_status_ids;
    }

    public long get_state_id() {
        return _state_id;
    }

    public void set_state_id(long _state_id) {
        this._state_id = _state_id;
    }

    public long get_city_id() {
        return _city_id;
    }

    public void set_city_id(long _city_id) {
        this._city_id = _city_id;
    }

    public String get_postal_code() {
        return _postal_code;
    }

    public void set_postal_code(String _postal_code) {
        this._postal_code = _postal_code;
    }

    public String get_choice_country_ids() {
        return _choice_country_ids;
    }

    public void set_choice_country_ids(String _choice_country_ids) {
        this._choice_country_ids = _choice_country_ids;
    }

    public String get_choice_state_ids() {
        return _choice_state_ids;
    }

    public void set_choice_state_ids(String _choice_state_ids) {
        this._choice_state_ids = _choice_state_ids;
    }

    public String get_choice_cities_ids() {
        return _choice_cities_ids;
    }

    public void set_choice_cities_ids(String _choice_cities_ids) {
        this._choice_cities_ids = _choice_cities_ids;
    }

    public String get_choice_caste_ids() {
        return _choice_caste_ids;
    }

    public void set_choice_caste_ids(String _choice_caste_ids) {
        this._choice_caste_ids = _choice_caste_ids;
    }

    public String get_choice_income_ids() {
        return _choice_income_ids;
    }

    public void set_choice_income_ids(String _choice_income_ids) {
        this._choice_income_ids = _choice_income_ids;
    }

    public String get_choice_family_values_ids() {
        return _choice_family_values_ids;
    }

    public void set_choice_family_values_ids(String _choice_family_values_ids) {
        this._choice_family_values_ids = _choice_family_values_ids;
    }

    public int get_body_id() {
        return _body_id;
    }

    public void set_body_id(int _body_id) {
        this._body_id = _body_id;
    }

    public int get_hijab_id() {
        return _hijab_id;
    }

    public void set_hijab_id(int _hijab_id) {
        this._hijab_id = _hijab_id;
    }

    public long get_issubscribed() {
        return _issubscribed;
    }

    public void set_issubscribed(long _issubscribed) {
        this._issubscribed = _issubscribed;
    }

    public long get_visting_memberid() {
        return _visting_memberid;
    }

    public void set_visting_memberid(long _visting_memberid) {
        this._visting_memberid = _visting_memberid;
    }

    public boolean is_rememberme() {
        return _rememberme;
    }

    public void set_rememberme(boolean _rememberme) {
        this._rememberme = _rememberme;
    }

    public int get_hair_color_id() {
        return _hair_color_id;
    }

    public void set_hair_color_id(int _hair_color_id) {
        this._hair_color_id = _hair_color_id;
    }

    public int get_eye_color_id() {
        return _eye_color_id;
    }

    public void set_eye_color_id(int _eye_color_id) {
        this._eye_color_id = _eye_color_id;
    }

    public int get_complexion_id() {
        return _complexion_id;
    }

    public void set_complexion_id(int _complexion_id) {
        this._complexion_id = _complexion_id;
    }

    public long get_height_id() {
        return _height_id;
    }

    public void set_height_id(long _height_id) {
        this._height_id = _height_id;
    }

    public long get_choice_age_from() {
        return _choice_age_from;
    }

    public void set_choice_age_from(long _choice_age_from) {
        this._choice_age_from = _choice_age_from;
    }

    public long get_choice_age_upto() {
        return _choice_age_upto;
    }

    public void set_choice_age_upto(long _choice_age_upto) {
        this._choice_age_upto = _choice_age_upto;
    }

    public String get_choice_body_ids() {
        return _choice_body_ids;
    }

    public void set_choice_body_ids(String _choice_body_ids) {
        this._choice_body_ids = _choice_body_ids;
    }

    public String get_choice_eye_color_ids() {
        return _choice_eye_color_ids;
    }

    public void set_choice_eye_color_ids(String _choice_eye_color_ids) {
        this._choice_eye_color_ids = _choice_eye_color_ids;
    }

    public String get_choice_hair_color_ids() {
        return _choice_hair_color_ids;
    }

    public void set_choice_hair_color_ids(String _choice_hair_color_ids) {
        this._choice_hair_color_ids = _choice_hair_color_ids;
    }

    public String get_choice_complexion_ids() {
        return _choice_complexion_ids;
    }

    public void set_choice_complexion_ids(String _choice_complexion_ids) {
        this._choice_complexion_ids = _choice_complexion_ids;
    }

    public long get_choice_height_from_id() {
        return _choice_height_from_id;
    }

    public void set_choice_height_from_id(long _choice_height_from_id) {
        this._choice_height_from_id = _choice_height_from_id;
    }

    public long get_choice_height_to_id() {
        return _choice_height_to_id;
    }

    public void set_choice_height_to_id(long _choice_height_to_id) {
        this._choice_height_to_id = _choice_height_to_id;
    }

    public String get_choice_preferences_ids() {
        return _choice_preferences_ids;
    }

    public void set_choice_preferences_ids(String _choice_preferences_ids) {
        this._choice_preferences_ids = _choice_preferences_ids;
    }

    public String get_choice_preferences() {
        return _choice_preferences;
    }

    public void set_choice_preferences(String _choice_preferences) {
        this._choice_preferences = _choice_preferences;
    }

    public long get_education_id() {
        return _education_id;
    }

    public void set_education_id(long _education_id) {
        this._education_id = _education_id;
    }

    public long get_education_field_id() {
        return _education_field_id;
    }

    public void set_education_field_id(long _education_field_id) {
        this._education_field_id = _education_field_id;
    }

    public int get_economy_id() {
        return _economy_id;
    }

    public void set_economy_id(int _economy_id) {
        this._economy_id = _economy_id;
    }

    public long get_occupation_id() {
        return _occupation_id;
    }

    public void set_occupation_id(long _occupation_id) {
        this._occupation_id = _occupation_id;
    }

    public long get_income_level_id() {
        return _income_level_id;
    }

    public void set_income_level_id(long _income_level_id) {
        this._income_level_id = _income_level_id;
    }

    public String get_income_level_unit() {
        return _income_level_unit;
    }

    public void set_income_level_unit(String _income_level_unit) {
        this._income_level_unit = _income_level_unit;
    }

    public String get_income_level_type() {
        return _income_level_type;
    }

    public void set_income_level_type(String _income_level_type) {
        this._income_level_type = _income_level_type;
    }

    public long get_religion_id() {
        return _religion_id;
    }

    public void set_religion_id(long _religion_id) {
        this._religion_id = _religion_id;
    }

    public int get_religious_sect_id() {
        return _religious_sect_id;
    }

    public void set_religious_sect_id(int _religious_sect_id) {
        this._religious_sect_id = _religious_sect_id;
    }

    public String getReligious_sec_type() {
        return religious_sec_type;
    }

    public void setReligious_sec_type(String religious_sec_type) {
        this.religious_sec_type = religious_sec_type;
    }

    public int get_ethnic_background_id() {
        return _ethnic_background_id;
    }

    public void set_ethnic_background_id(int _ethnic_background_id) {
        this._ethnic_background_id = _ethnic_background_id;
    }

    public long get_caste_id() {
        return _caste_id;
    }

    public void set_caste_id(long _caste_id) {
        this._caste_id = _caste_id;
    }

    public long get_sibling_id() {
        return _sibling_id;
    }

    public void set_sibling_id(long _sibling_id) {
        this._sibling_id = _sibling_id;
    }

    public String get_brothers_count() {
        return _brothers_count;
    }

    public void set_brothers_count(String _brothers_count) {
        this._brothers_count = _brothers_count;
    }

    public String get_sisters_count() {
        return _sisters_count;
    }

    public void set_sisters_count(String _sisters_count) {
        this._sisters_count = _sisters_count;
    }

    public int get_marital_status_id() {
        return _marital_status_id;
    }

    public void set_marital_status_id(int _marital_status_id) {
        this._marital_status_id = _marital_status_id;
    }

    public int get_children_id() {
        return _children_id;
    }

    public void set_children_id(int _children_id) {
        this._children_id = _children_id;
    }

    public int get_living_arrangement_id() {
        return _living_arrangement_id;
    }

    public void set_living_arrangement_id(int _living_arrangement_id) {
        this._living_arrangement_id = _living_arrangement_id;
    }

    public int get_family_values_id() {
        return _family_values_id;
    }

    public void set_family_values_id(int _family_values_id) {
        this._family_values_id = _family_values_id;
    }

    public int get_raised_id() {
        return _raised_id;
    }

    public void set_raised_id(int _raised_id) {
        this._raised_id = _raised_id;
    }

    public int get_smoking_id() {
        return _smoking_id;
    }

    public void set_smoking_id(int _smoking_id) {
        this._smoking_id = _smoking_id;
    }

    public int get_drink_id() {
        return _drink_id;
    }

    public void set_drink_id(int _drink_id) {
        this._drink_id = _drink_id;
    }

    public String get_choice_education_ids() {
        return _choice_education_ids;
    }

    public void set_choice_education_ids(String _choice_education_ids) {
        this._choice_education_ids = _choice_education_ids;
    }

    public String get_choice_occupation_ids() {
        return _choice_occupation_ids;
    }

    public void set_choice_occupation_ids(String _choice_occupation_ids) {
        this._choice_occupation_ids = _choice_occupation_ids;
    }


    public String get_choice_economy_ids() {
        return _choice_economy_ids;
    }

    public void set_choice_economy_ids(String _choice_economy_ids) {
        this._choice_economy_ids = _choice_economy_ids;
    }

    public String get_choice_religious_sect_ids() {
        return _choice_religious_sect_ids;
    }

    public void set_choice_religious_sect_ids(String _choice_religious_sect_ids) {
        this._choice_religious_sect_ids = _choice_religious_sect_ids;
    }

    public String get_choice_ethnic_bground_ids() {
        return _choice_ethnic_bground_ids;
    }

    public void set_choice_ethnic_bground_ids(String _choice_ethnic_bground_ids) {
        this._choice_ethnic_bground_ids = _choice_ethnic_bground_ids;
    }

    public String get_choice_sibling_ids() {
        return _choice_sibling_ids;
    }

    public void set_choice_sibling_ids(String _choice_sibling_ids) {
        this._choice_sibling_ids = _choice_sibling_ids;
    }

    public String get_choice_marital_status_ids() {
        return _choice_marital_status_ids;
    }

    public void set_choice_marital_status_ids(String _choice_marital_status_ids) {
        this._choice_marital_status_ids = _choice_marital_status_ids;
    }

    public String get_choice_children_ids() {
        return _choice_children_ids;
    }

    public void set_choice_children_ids(String _choice_children_ids) {
        this._choice_children_ids = _choice_children_ids;
    }

    public String get_choice_living_arangment_ids() {
        return _choice_living_arangment_ids;
    }

    public void set_choice_living_arangment_ids(String _choice_living_arangment_ids) {
        this._choice_living_arangment_ids = _choice_living_arangment_ids;
    }

    public String get_choice_hijab_ids() {
        return _choice_hijab_ids;
    }

    public void set_choice_hijab_ids(String _choice_hijab_ids) {
        this._choice_hijab_ids = _choice_hijab_ids;
    }

    public String get_choice_raised_ids() {
        return _choice_raised_ids;
    }

    public void set_choice_raised_ids(String _choice_raised_ids) {
        this._choice_raised_ids = _choice_raised_ids;
    }

    public String get_choice_smoking_ids() {
        return _choice_smoking_ids;
    }

    public void set_choice_smoking_ids(String _choice_smoking_ids) {
        this._choice_smoking_ids = _choice_smoking_ids;
    }

    public String get_choice_drink_ids() {
        return _choice_drink_ids;
    }

    public void set_choice_drink_ids(String _choice_drink_ids) {
        this._choice_drink_ids = _choice_drink_ids;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public long get_my_id() {
        return _my_id;
    }

    public void set_my_id(long _my_id) {
        this._my_id = _my_id;
    }

    public String get_choice_ids() {
        return _choice_ids;
    }

    public void set_choice_ids(String _choice_ids) {
        this._choice_ids = _choice_ids;
    }

    public boolean is_mychecked() {
        return _mychecked;
    }

    public void set_mychecked(boolean _mychecked) {
        this._mychecked = _mychecked;
    }

    public boolean is_choicechecked() {
        return _choicechecked;
    }

    public void set_choicechecked(boolean _choicechecked) {
        this._choicechecked = _choicechecked;
    }

    public long get_about_type_id() {
        return _about_type_id;
    }

    public void set_about_type_id(long _about_type_id) {
        this._about_type_id = _about_type_id;
    }

    public long get_about_member_id() {
        return _about_member_id;
    }

    public void set_about_member_id(long _about_member_id) {
        this._about_member_id = _about_member_id;
    }

    public String get_about_type() {
        return _about_type;
    }

    public void set_about_type(String _about_type) {
        this._about_type = _about_type;
    }

    public String get_about_member() {
        return _about_member;
    }

    public void set_about_member(String _about_member) {
        this._about_member = _about_member;
    }

    public long get_photo_id() {
        return _photo_id;
    }

    public void set_photo_id(long _photo_id) {
        this._photo_id = _photo_id;
    }

    public String get_photo_name() {
        return _photo_name;
    }

    public void set_photo_name(String _photo_name) {
        this._photo_name = _photo_name;
    }

    public String get_photo_ext() {
        return _photo_ext;
    }

    public void set_photo_ext(String _photo_ext) {
        this._photo_ext = _photo_ext;
    }

    public String get_photo_server_name() {
        return _photo_server_name;
    }

    public void set_photo_server_name(String _photo_server_name) {
        this._photo_server_name = _photo_server_name;
    }

    public long get_photo_file_size() {
        return _photo_file_size;
    }

    public void set_photo_file_size(long _photo_file_size) {
        this._photo_file_size = _photo_file_size;
    }

    public long get_photo_status() {
        return _photo_status;
    }

    public void set_photo_status(long _photo_status) {
        this._photo_status = _photo_status;
    }

    public String get_photo_create_date() {
        return _photo_create_date;
    }

    public void set_photo_create_date(String _photo_create_date) {
        this._photo_create_date = _photo_create_date;
    }

    public String get_joined_date() {
        return _joined_date;
    }

    public void set_joined_date(String _joined_date) {
        this._joined_date = _joined_date;
    }

    public String get_photo_path() {
        return _photo_path;
    }

    public void set_photo_path(String _photo_path) {
        this._photo_path = _photo_path;
    }

    public String get_photo_path_thb() {
        return _photo_path_thb;
    }

    public void set_photo_path_thb(String _photo_path_thb) {
        this._photo_path_thb = _photo_path_thb;
    }

    public String get_age() {
        return _age;
    }

    public void set_age(String _age) {
        this._age = _age;
    }

    public String get_country_name() {
        return _country_name;
    }

    public void set_country_name(String _country_name) {
        this._country_name = _country_name;
    }

    public String get_country_flag() {
        return _country_flag;
    }

    public void set_country_flag(String _country_flag) {
        this._country_flag = _country_flag;
    }

    public String get_country_flag1() {
        return _country_flag1;
    }

    public void set_country_flag1(String _country_flag1) {
        this._country_flag1 = _country_flag1;
    }

    public String get_state_name() {
        return _state_name;
    }

    public void set_state_name(String _state_name) {
        this._state_name = _state_name;
    }

    public String get_city_name() {
        return _city_name;
    }

    public void set_city_name(String _city_name) {
        this._city_name = _city_name;
    }

    public String get_visa_status_types() {
        return _visa_status_types;
    }

    public void set_visa_status_types(String _visa_status_types) {
        this._visa_status_types = _visa_status_types;
    }

    public long get_photo_count() {
        return _photo_count;
    }

    public void set_photo_count(long _photo_count) {
        this._photo_count = _photo_count;
    }

    public String get_body_types() {
        return _body_types;
    }

    public void set_body_types(String _body_types) {
        this._body_types = _body_types;
    }

    public String get_hair_color_types() {
        return _hair_color_types;
    }

    public void set_hair_color_types(String _hair_color_types) {
        this._hair_color_types = _hair_color_types;
    }

    public String get_eye_color_types() {
        return _eye_color_types;
    }

    public void set_eye_color_types(String _eye_color_types) {
        this._eye_color_types = _eye_color_types;
    }

    public String get_complexion_types() {
        return _complexion_types;
    }

    public void set_complexion_types(String _complexion_types) {
        this._complexion_types = _complexion_types;
    }

    public String get_height_description() {
        return _height_description;
    }

    public void set_height_description(String _height_description) {
        this._height_description = _height_description;
    }

    public String get_education_types() {
        return _education_types;
    }

    public void set_education_types(String _education_types) {
        this._education_types = _education_types;
    }

    public String get_education_field_types() {
        return _education_field_types;
    }

    public void set_education_field_types(String _education_field_types) {
        this._education_field_types = _education_field_types;
    }

    public String get_occupation_types() {
        return _occupation_types;
    }

    public void set_occupation_types(String _occupation_types) {
        this._occupation_types = _occupation_types;
    }

    public String get_income_level() {
        return _income_level;
    }

    public void set_income_level(String _income_level) {
        this._income_level = _income_level;
    }

    public String get_sibling_types() {
        return _sibling_types;
    }

    public void set_sibling_types(String _sibling_types) {
        this._sibling_types = _sibling_types;
    }

    public String get_caste_name() {
        return _caste_name;
    }

    public void set_caste_name(String _caste_name) {
        this._caste_name = _caste_name;
    }

    public String get_marital_status_types() {
        return _marital_status_types;
    }

    public void set_marital_status_types(String _marital_status_types) {
        this._marital_status_types = _marital_status_types;
    }

    public String get_children_types() {
        return _children_types;
    }

    public void set_children_types(String _children_types) {
        this._children_types = _children_types;
    }

    public String get_living_arrabgements_types() {
        return _living_arrabgements_types;
    }

    public void set_living_arrabgements_types(String _living_arrabgements_types) {
        this._living_arrabgements_types = _living_arrabgements_types;
    }

    public String get_hijab_types() {
        return _hijab_types;
    }

    public void set_hijab_types(String _hijab_types) {
        this._hijab_types = _hijab_types;
    }

    public String get_raised_types() {
        return _raised_types;
    }

    public void set_raised_types(String _raised_types) {
        this._raised_types = _raised_types;
    }

    public String get_family_values_types() {
        return _family_values_types;
    }

    public void set_family_values_types(String _family_values_types) {
        this._family_values_types = _family_values_types;
    }

    public String get_smoking_types() {
        return _smoking_types;
    }

    public void set_smoking_types(String _smoking_types) {
        this._smoking_types = _smoking_types;
    }

    public String get_registration_date() {
        return _registration_date;
    }

    public void set_registration_date(String _registration_date) {
        this._registration_date = _registration_date;
    }

    public String get_ethnic_background_types() {
        return _ethnic_background_types;
    }

    public void set_ethnic_background_types(String _ethnic_background_types) {
        this._ethnic_background_types = _ethnic_background_types;
    }

    public String get_drinks_types() {
        return _drinks_types;
    }

    public void set_drinks_types(String _drinks_types) {
        this._drinks_types = _drinks_types;
    }

    public long get_boys_count() {
        return _boys_count;
    }

    public void set_boys_count(long _boys_count) {
        this._boys_count = _boys_count;
    }

    public long get_girls_count() {
        return _girls_count;
    }

    public void set_girls_count(long _girls_count) {
        this._girls_count = _girls_count;
    }

    public long get_min_age() {
        return _min_age;
    }

    public void set_min_age(long _min_age) {
        this._min_age = _min_age;
    }

    public long get_max_age() {
        return _max_age;
    }

    public void set_max_age(long _max_age) {
        this._max_age = _max_age;
    }

    public long get_registration_within_id() {
        return _registration_within_id;
    }

    public void set_registration_within_id(long _registration_within_id) {
        this._registration_within_id = _registration_within_id;
    }

    public int get_pictureonly() {
        return _pictureonly;
    }

    public void set_pictureonly(int _pictureonly) {
        this._pictureonly = _pictureonly;
    }

    public int get_opentopublic() {
        return _opentopublic;
    }

    public void set_opentopublic(int _opentopublic) {
        this._opentopublic = _opentopublic;
    }

    public String get_choice_profile_owner_Ids() {
        return _choice_profile_owner_Ids;
    }

    public void set_choice_profile_owner_Ids(String _choice_profile_owner_Ids) {
        this._choice_profile_owner_Ids = _choice_profile_owner_Ids;
    }

    public long get_last_login_date_id() {
        return _last_login_date_id;
    }

    public void set_last_login_date_id(long _last_login_date_id) {
        this._last_login_date_id = _last_login_date_id;
    }

    public String get_choice_zodiac_sign_ids() {
        return _choice_zodiac_sign_ids;
    }

    public void set_choice_zodiac_sign_ids(String _choice_zodiac_sign_ids) {
        this._choice_zodiac_sign_ids = _choice_zodiac_sign_ids;
    }

    public String get_choice_height_from() {
        return _choice_height_from;
    }

    public void set_choice_height_from(String _choice_height_from) {
        this._choice_height_from = _choice_height_from;
    }

    public String get_choice_height_to() {
        return _choice_height_to;
    }

    public void set_choice_height_to(String _choice_height_to) {
        this._choice_height_to = _choice_height_to;
    }

    public String get_choice_country_names() {
        return _choice_country_names;
    }

    public void set_choice_country_names(String _choice_country_names) {
        this._choice_country_names = _choice_country_names;
    }

    public String get_choice_visa_status() {
        return _choice_visa_status;
    }

    public void set_choice_visa_status(String _choice_visa_status) {
        this._choice_visa_status = _choice_visa_status;
    }

    public String get_choice_body() {
        return _choice_body;
    }

    public void set_choice_body(String _choice_body) {
        this._choice_body = _choice_body;
    }

    public String get_choice_hair_color() {
        return _choice_hair_color;
    }

    public void set_choice_hair_color(String _choice_hair_color) {
        this._choice_hair_color = _choice_hair_color;
    }

    public String get_choice_eye_color() {
        return _choice_eye_color;
    }

    public void set_choice_eye_color(String _choice_eye_color) {
        this._choice_eye_color = _choice_eye_color;
    }

    public String get_choice_complexion() {
        return _choice_complexion;
    }

    public void set_choice_complexion(String _choice_complexion) {
        this._choice_complexion = _choice_complexion;
    }

    public String get_choice_education() {
        return _choice_education;
    }

    public void set_choice_education(String _choice_education) {
        this._choice_education = _choice_education;
    }

    public String get_choice_occupation() {
        return _choice_occupation;
    }

    public void set_choice_occupation(String _choice_occupation) {
        this._choice_occupation = _choice_occupation;
    }

    public String get_choice_religious_sec() {
        return _choice_religious_sec;
    }

    public void set_choice_religious_sec(String _choice_religious_sec) {
        this._choice_religious_sec = _choice_religious_sec;
    }

    public String get_choice_marital_status() {
        return _choice_marital_status;
    }

    public void set_choice_marital_status(String _choice_marital_status) {
        this._choice_marital_status = _choice_marital_status;
    }

    public String get_choice_children() {
        return _choice_children;
    }

    public void set_choice_children(String _choice_children) {
        this._choice_children = _choice_children;
    }

    public String get_choice_living_arrangements() {
        return _choice_living_arrangements;
    }

    public void set_choice_living_arrangements(String _choice_living_arrangements) {
        this._choice_living_arrangements = _choice_living_arrangements;
    }

    public String get_choice_family_values() {
        return _choice_family_values;
    }

    public void set_choice_family_values(String _choice_family_values) {
        this._choice_family_values = _choice_family_values;
    }

    public String get_choice_hijab() {
        return _choice_hijab;
    }

    public void set_choice_hijab(String _choice_hijab) {
        this._choice_hijab = _choice_hijab;
    }

    public String get_choice_raised() {
        return _choice_raised;
    }

    public void set_choice_raised(String _choice_raised) {
        this._choice_raised = _choice_raised;
    }

    public String get_choice_smoking() {
        return _choice_smoking;
    }

    public void set_choice_smoking(String _choice_smoking) {
        this._choice_smoking = _choice_smoking;
    }

    public String get_choice_ethnic_background() {
        return _choice_ethnic_background;
    }

    public void set_choice_ethnic_background(String _choice_ethnic_background) {
        this._choice_ethnic_background = _choice_ethnic_background;
    }

    public String get_choice_drinks() {
        return _choice_drinks;
    }

    public void set_choice_drinks(String _choice_drinks) {
        this._choice_drinks = _choice_drinks;
    }

    public long get_reviewed() {
        return _reviewed;
    }

    public void set_reviewed(long _reviewed) {
        this._reviewed = _reviewed;
    }

    public String get_created_date() {
        return _created_date;
    }

    public void set_created_date(String _created_date) {
        this._created_date = _created_date;
    }

    public String get_personality_ids() {
        return _personality_ids;
    }

    public void set_personality_ids(String _personality_ids) {
        this._personality_ids = _personality_ids;
    }

    public long get_total_member_count() {
        return _total_member_count;
    }

    public void set_total_member_count(long _total_member_count) {
        this._total_member_count = _total_member_count;
    }

    public int get_total_pages() {
        return _total_pages;
    }

    public void set_total_pages(int _total_pages) {
        this._total_pages = _total_pages;
    }

    public long get_page_no() {
        return _page_no;
    }

    public void set_page_no(long _page_no) {
        this._page_no = _page_no;
    }

    public long get_viewd() {
        return _viewd;
    }

    public void set_viewd(long _viewd) {
        this._viewd = _viewd;
    }

    public long get_hidden_status() {
        return _hidden_status;
    }

    public void set_hidden_status(long _hidden_status) {
        this._hidden_status = _hidden_status;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String get_sign_name() {
        return _sign_name;
    }

    public void set_sign_name(String _sign_name) {
        this._sign_name = _sign_name;
    }

    public String get_default_image() {
        return _default_image;
    }

    public void set_default_image(String _default_image) {
        this._default_image = _default_image;
    }

    public long get_request_response_id() {
        return _request_response_id;
    }

    public void set_request_response_id(long _request_response_id) {
        this._request_response_id = _request_response_id;
    }

    public long get_email_verified() {
        return _email_verified;
    }

    public void set_email_verified(long _email_verified) {
        this._email_verified = _email_verified;
    }

    public long get_phone_verified() {
        return _phone_verified;
    }

    public void set_phone_verified(long _phone_verified) {
        this._phone_verified = _phone_verified;
    }

    public long get_hide_phone() {
        return _hide_phone;
    }

    public void set_hide_phone(long _hide_phone) {
        this._hide_phone = _hide_phone;
    }

    public long get_phone_request_response_id() {
        return _phone_request_response_id;
    }

    public void set_phone_request_response_id(long _phone_request_response_id) {
        this._phone_request_response_id = _phone_request_response_id;
    }


    //private String _country_name1;

    //public String country_name1
    //{
    //    get { return _country_name1; }
    //    set { _country_name1 = value; }
    //}
    //private String _country_name2;

    public long get_phone_request_id() {
        return _phone_request_id;
    }

    public void set_phone_request_id(long _phone_request_id) {
        this._phone_request_id = _phone_request_id;
    }

    public long get_phone_privilege_id() {
        return _phone_privilege_id;
    }

    public void set_phone_privilege_id(long _phone_privilege_id) {
        this._phone_privilege_id = _phone_privilege_id;
    }

    public String get_phone_home() {
        return _phone_home;
    }

    public void set_phone_home(String _phone_home) {
        this._phone_home = _phone_home;
    }

    public long get_phone_office() {
        return _phone_office;
    }

    public void set_phone_office(long _phone_office) {
        this._phone_office = _phone_office;
    }

    public String get_phone_mobile() {
        return _phone_mobile;
    }

    public void set_phone_mobile(String _phone_mobile) {
        this._phone_mobile = _phone_mobile;
    }

    public long get_photo_request_id() {
        return _photo_request_id;
    }

    public void set_photo_request_id(long _photo_request_id) {
        this._photo_request_id = _photo_request_id;
    }

    public long get_photo_privilege_id() {
        return _photo_privilege_id;
    }

    public void set_photo_privilege_id(long _photo_privilege_id) {
        this._photo_privilege_id = _photo_privilege_id;
    }

    public long get_profile_request_id() {
        return _profile_request_id;
    }

    public void set_profile_request_id(long _profile_request_id) {
        this._profile_request_id = _profile_request_id;
    }

    public long get_photo_upload_request_id() {
        return _photo_upload_request_id;
    }

    public void set_photo_upload_request_id(long _photo_upload_request_id) {
        this._photo_upload_request_id = _photo_upload_request_id;
    }

    public long get_featured_profile() {
        return _featured_profile;
    }

    public void set_featured_profile(long _featured_profile) {
        this._featured_profile = _featured_profile;
    }

    public long get_accept_message() {
        return _accept_message;
    }

    public void set_accept_message(long _accept_message) {
        this._accept_message = _accept_message;
    }

    public long get_open_message() {
        return _open_message;
    }

    public void set_open_message(long _open_message) {
        this._open_message = _open_message;
    }

    public String get_notes() {
        return _notes;
    }

    public void set_notes(String _notes) {
        this._notes = _notes;
    }

    public long get_member_notes_id() {
        return _member_notes_id;
    }

    public void set_member_notes_id(long _member_notes_id) {
        this._member_notes_id = _member_notes_id;
    }

    public String get_deactivate_reason() {
        return _deactivate_reason;
    }

    public void set_deactivate_reason(String _deactivate_reason) {
        this._deactivate_reason = _deactivate_reason;
    }

    public long get_deactivate_reason_id() {
        return _deactivate_reason_id;
    }

    public void set_deactivate_reason_id(long _deactivate_reason_id) {
        this._deactivate_reason_id = _deactivate_reason_id;
    }

    public long get_no_data() {
        return _no_data;
    }

    public void set_no_data(long _no_data) {
        this._no_data = _no_data;
    }

    public long get_allow() {
        return _allow;
    }

    public void set_allow(long _allow) {
        this._allow = _allow;
    }

    public String get_other_info() {
        return _other_info;
    }

    public void set_other_info(String _other_info) {
        this._other_info = _other_info;
    }

    public String get_good_quality() {
        return _good_quality;
    }

    public void set_good_quality(String _good_quality) {
        this._good_quality = _good_quality;
    }

    public String get_most_thankfull() {
        return _most_thankfull;
    }

    public void set_most_thankfull(String _most_thankfull) {
        this._most_thankfull = _most_thankfull;
    }

    public String get_for_fun() {
        return _for_fun;
    }

    public void set_for_fun(String _for_fun) {
        this._for_fun = _for_fun;
    }

    public String get_last_login_date() {
        return _last_login_date;
    }

    public void set_last_login_date(String _last_login_date) {
        this._last_login_date = _last_login_date;
    }

    public long get_blocked_member() {
        return _blocked_member;
    }

    public void set_blocked_member(long _blocked_member) {
        this._blocked_member = _blocked_member;
    }

    public long get_removed_member() {
        return _removed_member;
    }

    public void set_removed_member(long _removed_member) {
        this._removed_member = _removed_member;
    }

    public long get_saved_member() {
        return _saved_member;
    }

    public void set_saved_member(long _saved_member) {
        this._saved_member = _saved_member;
    }

    public long get_hide_photo() {
        return _hide_photo;
    }

    public void set_hide_photo(long _hide_photo) {
        this._hide_photo = _hide_photo;
    }

    public long get_hide_profile() {
        return _hide_profile;
    }

    public void set_hide_profile(long _hide_profile) {
        this._hide_profile = _hide_profile;
    }

    public long get_image_privilege_id() {
        return _image_privilege_id;
    }

    public void set_image_privilege_id(long _image_privilege_id) {
        this._image_privilege_id = _image_privilege_id;
    }

    public long get_profile_privilege_id() {
        return _profile_privilege_id;
    }

    public void set_profile_privilege_id(long _profile_privilege_id) {
        this._profile_privilege_id = _profile_privilege_id;
    }

    public long get_request_image() {
        return _request_image;
    }

    public void set_request_image(long _request_image) {
        this._request_image = _request_image;
    }

    public long get_request_image_view() {
        return _request_image_view;
    }

    public void set_request_image_view(long _request_image_view) {
        this._request_image_view = _request_image_view;
    }

    public long get_request_profile_view() {
        return _request_profile_view;
    }

    public void set_request_profile_view(long _request_profile_view) {
        this._request_profile_view = _request_profile_view;
    }

    public long get_request_phone_view() {
        return _request_phone_view;
    }

    public void set_request_phone_view(long _request_phone_view) {
        this._request_phone_view = _request_phone_view;
    }

    public long get_request_id() {
        return _request_id;
    }

    public void set_request_id(long _request_id) {
        this._request_id = _request_id;
    }

    public long get_interest_response() {
        return _interest_response;
    }

    public void set_interest_response(long _interest_response) {
        this._interest_response = _interest_response;
    }

    public long get_interested_id() {
        return _interested_id;
    }

    public void set_interested_id(long _interested_id) {
        this._interested_id = _interested_id;
    }

    public long get_interest_received() {
        return _interest_received;
    }

    public void set_interest_received(long _interest_received) {
        this._interest_received = _interest_received;
    }

    public long get_image_count() {
        return _image_count;
    }

    public void set_image_count(long _image_count) {
        this._image_count = _image_count;
    }

    public long get_allow_phone_view() {
        return _allow_phone_view;
    }

    public void set_allow_phone_view(long _allow_phone_view) {
        this._allow_phone_view = _allow_phone_view;
    }

    public long get_cond_photo_privilege() {
        return _cond_photo_privilege;
    }

    public void set_cond_photo_privilege(long _cond_photo_privilege) {
        this._cond_photo_privilege = _cond_photo_privilege;
    }

    public long get_cond_phone_privilege() {
        return _cond_phone_privilege;
    }

    public void set_cond_phone_privilege(long _cond_phone_privilege) {
        this._cond_phone_privilege = _cond_phone_privilege;
    }

    public long get_image_view() {
        return _image_view;
    }

    public void set_image_view(long _image_view) {
        this._image_view = _image_view;
    }

    public long get_phone_view() {
        return _phone_view;
    }

    public void set_phone_view(long _phone_view) {
        this._phone_view = _phone_view;
    }

    public String get_pref1() {
        return _pref1;
    }

    public void set_pref1(String _pref1) {
        this._pref1 = _pref1;
    }

    public String get_pref2() {
        return _pref2;
    }

    public void set_pref2(String _pref2) {
        this._pref2 = _pref2;
    }

    public String get_pref3() {
        return _pref3;
    }

    public void set_pref3(String _pref3) {
        this._pref3 = _pref3;
    }

    public String get_pref4() {
        return _pref4;
    }

    public void set_pref4(String _pref4) {
        this._pref4 = _pref4;
    }

    public String get_prefvalue1() {
        return _prefvalue1;
    }

    public void set_prefvalue1(String _prefvalue1) {
        this._prefvalue1 = _prefvalue1;
    }

    public String get_prefvalue2() {
        return _prefvalue2;
    }

    public void set_prefvalue2(String _prefvalue2) {
        this._prefvalue2 = _prefvalue2;
    }

    public String get_prefvalue3() {
        return _prefvalue3;
    }

    public void set_prefvalue3(String _prefvalue3) {
        this._prefvalue3 = _prefvalue3;
    }

    public String get_prefvalue4() {
        return _prefvalue4;
    }

    public void set_prefvalue4(String _prefvalue4) {
        this._prefvalue4 = _prefvalue4;
    }

    public String get_to_member_id_dec() {
        return _to_member_id_dec;
    }

    public void set_to_member_id_dec(String _to_member_id_dec) {
        this._to_member_id_dec = _to_member_id_dec;
    }

    public String get_start_date() {
        return _start_date;
    }

    public void set_start_date(String _start_date) {
        this._start_date = _start_date;
    }

    public String get_end_date() {
        return _end_date;
    }

    public void set_end_date(String _end_date) {
        this._end_date = _end_date;
    }

    public long get_blur() {
        return _blur;
    }

    public void set_blur(long _blur) {
        this._blur = _blur;
    }

    public long get_isedit() {
        return _isedit;
    }

    public void set_isedit(long _isedit) {
        this._isedit = _isedit;
    }

    public long get_privilege_type_id() {
        return _privilege_type_id;
    }

    public void set_privilege_type_id(long _privilege_type_id) {
        this._privilege_type_id = _privilege_type_id;
    }

}
