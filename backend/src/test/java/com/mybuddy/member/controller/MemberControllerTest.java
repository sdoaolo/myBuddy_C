package com.mybuddy.member.controller;

import com.google.gson.Gson;
import com.mybuddy.global.mockdata.MockTestData;
import com.mybuddy.member.dto.*;
import com.mybuddy.member.entity.Member;
import com.mybuddy.member.mapper.MemberMapper;
import com.mybuddy.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@WithMockUser(username = "kimcoding@mybuddy.com", roles = {"USER"})
public class MemberControllerTest {

    private final String MEMBER_DEFAULT_URL = "/api/v1/members";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberMapper mapper;

    @Autowired
    private Gson gson;

    @DisplayName("회원 등록")
    @Test
    public void createMemberTest() throws Exception {
        // Given
        String content = gson.toJson(MockTestData.MockMember.getMemberCreateDto());

        MockMultipartFile createDto = new MockMultipartFile("createDto", null,
                MediaType.APPLICATION_JSON_VALUE, content.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile profileImage = new MockMultipartFile("profileImage", "image.png",
                MediaType.IMAGE_PNG_VALUE, "image".getBytes(StandardCharsets.UTF_8));

        given(mapper.memberCreateDtoToMember(Mockito.any(MemberCreateDto.class)))
                .willReturn(new Member());
        given(memberService.createMember(Mockito.any(Member.class), Mockito.any(MultipartFile.class)))
                .willReturn(MockTestData.MockMember.getMember());

        ConstraintDescriptions createMemberConstraints =
                new ConstraintDescriptions(MemberCreateDto.class);
        List<String> emailDescriptions = createMemberConstraints.descriptionsForProperty("email");
        List<String> passwordDescriptions = createMemberConstraints.descriptionsForProperty("password");
        List<String> nicknameDescriptions = createMemberConstraints.descriptionsForProperty("nickname");
        List<String> dogNameDescriptions = createMemberConstraints.descriptionsForProperty("dogName");
        List<String> dogGenderDescriptions = createMemberConstraints.descriptionsForProperty("dogGender");

        // When
        ResultActions actions =
                mockMvc.perform(
                        multipart(MEMBER_DEFAULT_URL)
                                .file(createDto)
                                .file(profileImage)
                                .with(csrf())
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                );

        // Then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", is(startsWith(MEMBER_DEFAULT_URL))))
                .andDo(document("create-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestPartBody(
                                "createDto"
                        ),
                        requestPartFields("createDto",
                                List.of(
                                        fieldWithPath("email")
                                                .type(JsonFieldType.STRING)
                                                .description("이메일")
                                                .attributes(key("constraints").value(emailDescriptions)),
                                        fieldWithPath("password")
                                                .type(JsonFieldType.STRING)
                                                .description("암호")
                                                .attributes(key("constraints").value(passwordDescriptions)),
                                        fieldWithPath("nickname")
                                                .type(JsonFieldType.STRING)
                                                .description("견주 닉네임")
                                                .attributes(key("constraints").value(nicknameDescriptions)),
                                        fieldWithPath("dogName")
                                                .type(JsonFieldType.STRING)
                                                .description("강아지 이름")
                                                .attributes(key("constraints").value(dogNameDescriptions)),
                                        fieldWithPath("dogGender")
                                                .type(JsonFieldType.STRING)
                                                .description("강아지 성별")
                                                .attributes(key("constraints").value(dogGenderDescriptions))
                                )),
                        relaxedRequestParts(
                                partWithName("profileImage").description("첨부 파일")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header. 등록된 리소스의 URI")
                        )));
    }

    @DisplayName("회원 정보 수정")
    @Test
    public void updateMemberTest() throws Exception {
        // Given
        String content = gson.toJson(MockTestData.MockMember.getMemberPatchDto());
        Long mockLoginUserId = 2L;

        MockMultipartFile patchDto = new MockMultipartFile("updateDto", null,
                MediaType.APPLICATION_JSON_VALUE, content.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile profileImage = new MockMultipartFile("profileImage", "image.png",
                MediaType.IMAGE_PNG_VALUE, "image" .getBytes(StandardCharsets.UTF_8));

        given(mapper.memberUpdateDtoToMember(Mockito.any(MemberUpdateDto.class)))
                .willReturn(new Member());
        given(memberService.updateMember(Mockito.any(Member.class),
                Mockito.any(MultipartFile.class), Mockito.anyLong()))
                .willReturn(MockTestData.MockMember.getMember());

        ConstraintDescriptions patchMemberConstraints =
                new ConstraintDescriptions(MemberCreateDto.class);
        List<String> nicknameDescriptions = patchMemberConstraints.descriptionsForProperty("nickname");
        List<String> dogNameDescriptions = patchMemberConstraints.descriptionsForProperty("dogName");
        List<String> addressDescriptions = patchMemberConstraints.descriptionsForProperty("address");
        List<String> aboutMeDescriptions = patchMemberConstraints.descriptionsForProperty("aboutMe");

        // When
        ResultActions actions =
                mockMvc.perform(
                        multipart(MEMBER_DEFAULT_URL + "/" +
                                MockTestData.MockMember.getMember().getMemberId())
                                .file(patchDto)
                                .file(profileImage)
                                .header("Authorization", "")
                                .with(csrf())
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                );

        // Then
        actions
                .andExpect(status().isOk())
                .andDo(document("update-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .attributes(key("constraints").value(""))
                                        .description("Access Token")
                        ),
                        requestPartBody(
                                "updateDto"
                        ),
                        requestPartFields("updateDto",
                                List.of(
                                        fieldWithPath("nickname")
                                                .type(JsonFieldType.STRING)
                                                .description("견주 닉네임")
                                                .attributes(key("constraints").value(nicknameDescriptions)),
                                        fieldWithPath("dogName")
                                                .type(JsonFieldType.STRING)
                                                .description("강아지 이름")
                                                .attributes(key("constraints").value(dogNameDescriptions)),
                                        fieldWithPath("address")
                                                .type(JsonFieldType.STRING)
                                                .description("주소")
                                                .attributes(key("constraints").value(addressDescriptions)),
                                        fieldWithPath("aboutMe")
                                                .type(JsonFieldType.STRING)
                                                .description("강아지 소개")
                                                .attributes(key("constraints").value(aboutMeDescriptions))
                                )),
                        relaxedRequestParts(
                                partWithName("profileImage").description("첨부 파일")
                        )));
    }

    @DisplayName("회원 상세 조회")
    @Test
    public void getMemberTest() throws Exception {
        // Given
        MemberResponseDto responseDto = MockTestData.MockMember.getMemberResponseDto();

        given(memberService.getMember(Mockito.anyLong()))
                .willReturn(new Member());
        given(mapper.memberToMemberResponseDto(Mockito.any(Member.class),Mockito.anyLong()))
                .willReturn(responseDto);

        // When
        ResultActions actions =
                mockMvc.perform(
                        get(MEMBER_DEFAULT_URL + "/{member-id}",
                                MockTestData.MockMember.getMember().getMemberId())
                                .header("Authorization", "")
                                .with(csrf())
                                .accept(MediaType.APPLICATION_JSON)
                );

        // Then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.nickname")
                        .value(responseDto.getNickname()))
                .andExpect(jsonPath("$.data.dogName")
                        .value(responseDto.getDogName()))
                .andExpect(jsonPath("$.data.dogGender")
                        .value(responseDto.getDogGender().toString()))
                .andExpect(jsonPath("$.data.aboutMe")
                        .value(responseDto.getAboutMe()))
                .andExpect(jsonPath("$.data.followByUser")
                        .value(responseDto.getFollowByUser()))
                .andExpect(jsonPath("$.data.followerNumber")
                        .value(responseDto.getFollowerNumber()))
                .andExpect(jsonPath("$.data.followeeNumber")
                        .value(responseDto.getFolloweeNumber()))
                .andExpect(jsonPath("$.data.profileUrl")
                        .value(responseDto.getProfileUrl()))
                .andExpect(jsonPath("$.data.bulletinPostForMyPageResponseDtos[0].bulletinPostId")
                        .value(responseDto.getBulletinPostForMyPageResponseDtos().get(0).getBulletinPostId()))
                .andExpect(jsonPath("$.data.bulletinPostForMyPageResponseDtos[1].bulletinPostId")
                        .value(responseDto.getBulletinPostForMyPageResponseDtos().get(1).getBulletinPostId()))
                .andExpect(jsonPath("$.data.amenityForMyPageResponseDtos[0].amenityId")
                        .value(responseDto.getAmenityForMyPageResponseDtos().get(0).getAmenityId()))
                .andExpect(jsonPath("$.data.amenityForMyPageResponseDtos[1].amenityId")
                        .value(responseDto.getAmenityForMyPageResponseDtos().get(1).getAmenityId()))
                .andDo(document("get-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .attributes(key("constraints").value(""))
                                        .description("Access Token")
                        ),
                        pathParameters(
                                List.of(parameterWithName("member-id").description("회원 식별자"))
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("code")
                                                .type(JsonFieldType.STRING)
                                                .description("HTTP Status"),
                                        fieldWithPath("message")
                                                .type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data")
                                                .type(JsonFieldType.OBJECT)
                                                .description("회원 정보"),
                                        fieldWithPath("data.nickname")
                                                .type(JsonFieldType.STRING)
                                                .description("견주 닉네임"),
                                        fieldWithPath("data.dogName")
                                                .type(JsonFieldType.STRING)
                                                .description("강아지 이름"),
                                        fieldWithPath("data.dogGender")
                                                .type(JsonFieldType.STRING)
                                                .description("강아지 성별"),
                                        fieldWithPath("data.aboutMe")
                                                .type(JsonFieldType.STRING)
                                                .description("강아지 소개"),
                                        fieldWithPath("data.followByUser")
                                                .type(JsonFieldType.NUMBER)
                                                .description("로그인한 유저가 마이페이지 회원을 팔로우 했는지 여부"),
                                        fieldWithPath("data.followerNumber")
                                                .type(JsonFieldType.NUMBER)
                                                .description("팔로워 수"),
                                        fieldWithPath("data.followeeNumber")
                                                .type(JsonFieldType.NUMBER)
                                                .description("팔로잉 수"),
                                        fieldWithPath("data.profileUrl")
                                                .type(JsonFieldType.STRING)
                                                .description("Profile URL 주소"),
                                        fieldWithPath("data.bulletinPostForMyPageResponseDtos[].bulletinPostId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("게시물 식별자"),
                                        fieldWithPath("data.bulletinPostForMyPageResponseDtos[].photoUrl")
                                                .type(JsonFieldType.STRING)
                                                .description("게시물 사진 URL"),
                                        fieldWithPath("data.amenityForMyPageResponseDtos[].amenityId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("편의시설 식별자"),
                                        fieldWithPath("data.amenityForMyPageResponseDtos[].amenityName")
                                                .type(JsonFieldType.STRING)
                                                .description("편의시설 이름"),
                                        fieldWithPath("data.amenityForMyPageResponseDtos[].address")
                                                .type(JsonFieldType.STRING)
                                                .description("편의시설 주소"),
                                        fieldWithPath("data.amenityForMyPageResponseDtos[].photoUrl")
                                                .type(JsonFieldType.STRING)
                                                .description("편의시설 사진 URL"),
                                        fieldWithPath("data.amenityForMyPageResponseDtos[].postCreatedAt")
                                                .type(JsonFieldType.STRING)
                                                .description("편의시설 추천한 게시물 등록 시간 (내부용)")
                                )
                        )
                ));
    }

    @DisplayName("회원 리스트 조회 (운영자용)")
    @Test
    public void getMembersTest() throws Exception {
        // Given
        List<MemberListResponseDto> listResponseDtos = MockTestData.MockMember.getMemberListResponseDtos();

        given(memberService.getMemberList(Mockito.anyInt(), Mockito.anyInt()))
                .willReturn(MockTestData.MockMember.getPageMembers());
        given(mapper.membersToMemberListResponseDtos(Mockito.anyList()))
                .willReturn(listResponseDtos);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", "1");
        queryParams.add("size", "10");

        // When
        ResultActions actions =
                mockMvc.perform(
                        get(MEMBER_DEFAULT_URL)
                                .params(queryParams)
                                .header("Authorization", "")
                                .with(csrf())
                                .accept(MediaType.APPLICATION_JSON)
                );

        // Then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.*").exists())
                .andExpect(jsonPath("$.data.*", hasSize(2)))
                .andExpect(jsonPath("$.data[0].nickname")
                        .value(listResponseDtos.get(0).getNickname()))
                .andExpect(jsonPath("$.data[1].nickname")
                        .value(listResponseDtos.get(1).getNickname()))
                .andExpect(jsonPath("$.data[0].dogName")
                        .value(listResponseDtos.get(0).getDogName()))
                .andExpect(jsonPath("$.data[1].dogName")
                        .value(listResponseDtos.get(1).getDogName()))
                .andExpect(jsonPath("$.data[0].followerNumber")
                        .value(listResponseDtos.get(0).getFollowerNumber()))
                .andExpect(jsonPath("$.data[1].followerNumber")
                        .value(listResponseDtos.get(1).getFollowerNumber()))
                .andExpect(jsonPath("$.data[0].followeeNumber")
                        .value(listResponseDtos.get(0).getFolloweeNumber()))
                .andExpect(jsonPath("$.data[1].followeeNumber")
                        .value(listResponseDtos.get(1).getFolloweeNumber()))
                .andExpect(jsonPath("$.data[0].profileUrl")
                        .value(listResponseDtos.get(0).getProfileUrl()))
                .andExpect(jsonPath("$.data[1].profileUrl")
                        .value(listResponseDtos.get(1).getProfileUrl()))
                .andExpect(jsonPath("$.data[0].memberStatus")
                        .value(listResponseDtos.get(0).getMemberStatus().toString()))
                .andExpect(jsonPath("$.data[1].memberStatus")
                        .value(listResponseDtos.get(1).getMemberStatus().toString()))
                .andDo(document("get-members-for-admin",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .attributes(key("constraints").value(""))
                                        .description("Access Token")
                        ),
                        requestParameters(
                                List.of(
                                        parameterWithName("page").description("페이지 번호"),
                                        parameterWithName("size").description("페이지 당 회원 수"),
                                        parameterWithName("_csrf").ignored()
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("code")
                                                .type(JsonFieldType.STRING)
                                                .description("HTTP Status"),
                                        fieldWithPath("message")
                                                .type(JsonFieldType.STRING)
                                                .description("메시지"),
                                        fieldWithPath("data[]")
                                                .type(JsonFieldType.ARRAY)
                                                .description("회원 리스트"),
                                        fieldWithPath("data[].nickname")
                                                .type(JsonFieldType.STRING)
                                                .description("견주 닉네임"),
                                        fieldWithPath("data[].dogName")
                                                .type(JsonFieldType.STRING)
                                                .description("강아지 이름"),
                                        fieldWithPath("data[].followerNumber")
                                                .type(JsonFieldType.NUMBER)
                                                .description("팔로워 수"),
                                        fieldWithPath("data[].followeeNumber")
                                                .type(JsonFieldType.NUMBER)
                                                .description("팔로잉 수"),
                                        fieldWithPath("data[].profileUrl")
                                                .type(JsonFieldType.STRING)
                                                .description("Profile URL 주소"),
                                        fieldWithPath("data[].memberStatus")
                                                .type(JsonFieldType.STRING)
                                                .description("회원 계정 활성 상태"),
                                        fieldWithPath("pageInfo.page")
                                                .type(JsonFieldType.NUMBER)
                                                .description("현재 페이지"),
                                        fieldWithPath("pageInfo.size")
                                                .type(JsonFieldType.NUMBER)
                                                .description("페이지 당 회원 수"),
                                        fieldWithPath("pageInfo.totalElements")
                                                .type(JsonFieldType.NUMBER)
                                                .description("총 회원 수"),
                                        fieldWithPath("pageInfo.totalPages")
                                                .type(JsonFieldType.NUMBER)
                                                .description("총 페이지 수")
                                )
                        )
                ));
    }

    @DisplayName("회원 삭제")
    @Test
    public void deleteMemberTest() throws Exception {
        // Given
        Member member = MockTestData.MockMember.getMember();
        Long mockLoginUserId = 2L;

        doNothing().when(memberService).deleteMember(member.getMemberId(), mockLoginUserId);

        // When
        ResultActions actions =
                mockMvc.perform(
                        delete(MEMBER_DEFAULT_URL + "/{member-id}", member.getMemberId())
                                .header("Authorization", "")
                                .with(csrf())
                                .accept(MediaType.APPLICATION_JSON)
                );

        // Then
        actions
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andDo(document("delete-member",
                        preprocessRequest(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .attributes(key("constraints").value(""))
                                        .description("Access Token")
                        ),
                        pathParameters(
                                parameterWithName("member-id").description("회원 식별자")
                        )
                ));
    }
}
