package kr.board.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import kr.board.entity.Member;
import kr.board.mapper.MemberMapper;

@Controller
public class MemberController {
	
	@Autowired
	MemberMapper memberMapper;
	
	@RequestMapping("/memJoin.do")
	public String memJoin() {
		return "member/join";
	}
	
	@RequestMapping("/memRegisterCheck.do")
	public @ResponseBody int memRegisterCheck(@RequestParam("memID") String memID) {
		Member m = memberMapper.registerCheck(memID);
		if(m != null || memID.equals("")) {
			return 0; // 이미 존재하는 회원, 입력불가
		}
		return 1; // 사용가능한 아이디
	}
	
	@RequestMapping("/memRegister.do")
	public String memRegister(Member m, RedirectAttributes rttr, HttpSession session, String memPassword1, String memPassword2) {
		
		System.out.println(m.toString());
	    System.out.println("memPassword1: " + memPassword1);
	    System.out.println("memPassword2: " + memPassword2);
		
		if(m.getMemID()==null || m.getMemID().equals("") ||
		   memPassword1==null || memPassword1.equals("") ||
		   memPassword2==null || memPassword2.equals("") ||
		   m.getMemName()==null || m.getMemName().equals("") ||
		   m.getMemAge()==0 || 
		   m.getMemGender()==null || m.getMemGender().equals("") ||
		   m.getMemEmail()==null || m.getMemEmail().equals("")) {
		   // 누락메세지 가지고 가기? => 객체바인딩(Model, HttpServletRequest, HttpSession)
			rttr.addFlashAttribute("msgType", "실패 메세지");
			rttr.addFlashAttribute("msg", "모든 내용을 입력하세요.");
 			return "redirect:/memJoin.do";
		}
		if(!memPassword1.equals(memPassword2)) {
			rttr.addFlashAttribute("msgType", "실패 메세지");
			rttr.addFlashAttribute("msg", "비밀번호가 서로 다릅니다.");
			return "redirect:/memJoin.do";
		}
		m.setMemProfile(""); // 사진이미지는 없다는 의미 ""
		// 회원을 테이블에 저장하기
		int result = memberMapper.register(m);
		if(result==1) { // 회원가입 성공 메세지
			rttr.addFlashAttribute("msgType", "성공 메세지");
			rttr.addFlashAttribute("msg", "회원가입에 성공했습니다.");
			// 회원가입이 성공하면 => 로그인 처리 하기 
			session.setAttribute("mvo", m); // ${mvo} 가 empty인지 아닌지 체크 세션에다가 set attribute 하면 회원가입했는지 안했는지
			return "redirect:/";
		} else {
			rttr.addFlashAttribute("msgType", "실패 메세지");
			rttr.addFlashAttribute("msg", "이미 존재하는 회원입니다.");
			return "redirect:/memJoin.do";
		}
	}
	
	@RequestMapping("/memLogout.do")
	public String memLogout(HttpSession session) {
		session.invalidate(); // 세션 자체를 무효화 후 제거, removeAtrribute()는 현재 세션에서 특정 key-value만 제거
							// session은 Map 형식으로 생성된다. 키-값 
		return "redirect:/";
	}
	
	// 로그인 화면으로 이동
	@RequestMapping("/memLoginForm.do")
	public String memLoginForm() {
		return "member/memLoginForm";
	}
	
	//  로그인 기능 구현
	@RequestMapping("/memLogin.do")
	public String memLogin(Member m, RedirectAttributes rttr, HttpSession session) {
		if(m.getMemID()==null || m.getMemID().equals("") || 
		   m.getMemPassword()==null || m.getMemPassword().equals("")) {
			   rttr.addFlashAttribute("msgType", "실패 메세지");
			   rttr.addFlashAttribute("msg", "모든 내용을 입력해주세요.");
			   return "redirect:/memLoginForm.do";
		   }
		Member mvo = memberMapper.memLogin(m);
		if(mvo != null) { // 로그인 성공
			rttr.addFlashAttribute("msgType", "성공 메세지");
			rttr.addFlashAttribute("msg", "환영합니다.");
			session.setAttribute("mvo", mvo); // ${!empty mvo}
			return "redirect:/";
		} else { // 로그인 실패
			rttr.addFlashAttribute("msgType", "실패 메세지");
			rttr.addFlashAttribute("msg", "다시 로그인 해주세요.");
			return "redirect:/memLoginForm.do";
		}
	}
	
	// 회원정보수정화면
	@RequestMapping("/memUpdateForm.do")
	public String memUpdateForm() {
		return "member/memUpdateForm";
	}
	
	// 회원정보수정
	@RequestMapping("/memUpdate.do")
	public String memUpdate(Member m, RedirectAttributes rttr,
			String memPassword1, String memPassword2, HttpSession session) {
		
		if(m.getMemID()==null || m.getMemID().equals("") ||
				   memPassword1==null || memPassword1.equals("") ||
				   memPassword2==null || memPassword2.equals("") ||
				   m.getMemName()==null || m.getMemName().equals("") ||
				   m.getMemAge()==0 || 
				   m.getMemGender()==null || m.getMemGender().equals("") ||
				   m.getMemEmail()==null || m.getMemEmail().equals("")) {
				   // 누락메세지 가지고 가기? => 객체바인딩(Model, HttpServletRequest, HttpSession)
					rttr.addFlashAttribute("msgType", "실패 메세지");
					rttr.addFlashAttribute("msg", "모든 내용을 입력하세요.");
		 			return "redirect:/memUpdateForm.do";
				}
				if(!memPassword1.equals(memPassword2)) {
					rttr.addFlashAttribute("msgType", "실패 메세지");
					rttr.addFlashAttribute("msg", "비밀번호가 서로 다릅니다.");
					return "redirect:/memUpdateForm.do";
				}
				// 회원을 수정 후 저장하기
				int result = memberMapper.memUpdate(m);
				if(result==1) { // 수정 성공 메세지
					rttr.addFlashAttribute("msgType", "성공 메세지");
					rttr.addFlashAttribute("msg", "회원정보 수정에 성공했습니다.");
					// 회원정보 수정이 성공하면 => 로그인 처리 하기 
					session.setAttribute("mvo", m); // ${mvo} 가 empty인지 아닌지 체크 세션에다가 set attribute 하면 회원가입했는지 안했는지
					return "redirect:/";
				} else {
					rttr.addFlashAttribute("msgType", "실패 메세지");
					rttr.addFlashAttribute("msg", "회원정보 수정에 실패했습니다.");
					return "redirect:/memUpdateForm.do";
				}
	}
	
	// 회원의 사진등록 화면
	@RequestMapping("/memImageForm.do")
	public String memImageForm() {
		return "member/memImageForm";
	}
	
	// 회원사진 이미지 업로드(upload, DB저장)
	@RequestMapping("/memImageUpdate.do")
	public String memImageUpdate(HttpServletRequest request, HttpSession session, RedirectAttributes rttr) {
		// 파일업로드 API (3가지)
		MultipartRequest multi = null;
		int fileMaxSize=10*1024*1024;; // 10MB
		String savePath = request.getRealPath("resources/upload");
		try {
			// 이미지 업로드
			multi = new MultipartRequest(request, savePath, fileMaxSize, "UTF-8", new DefaultFileRenamePolicy());
		} catch (Exception e) {
			e.printStackTrace();
			rttr.addFlashAttribute("msgType", "실패 메세지");
			rttr.addFlashAttribute("msg", "파일의 크기는 10MB를 넘을 수 없습니다.");
			return "redirect:/memImageForm.do";
		}
		
		// 데이터베이스 테이블에 회원 이미지를 업데이트
		String memID = multi.getParameter("memID");
		String newProfile="";
		File file = multi.getFile("memProfile");
		if(file != null) { // 업로드가 된 상태(.png, .jpg, .gif)
			// 이미지파일 여부 체크 > 이미지 파일이 아니면 삭제
			String ext = file.getName().substring(file.getName().lastIndexOf(".")+1); // 확장자 추출
			System.out.println("ext:" + ext); // png
			ext = ext.toUpperCase(); // PNG, GIF, JPG
			if(ext.equals("PNG") || ext.equals("GIF") || ext.equals("JPG")) {
				// 새로 업로드 된 이미지(new 1.png), 현재 DB에있는 이미지(old 4.png)
				String oldProfile = memberMapper.getMember(memID).getMemProfile();
				System.out.println("oldProfile:" + oldProfile);
				File oldFile = new File(savePath+"/"+oldProfile);
				if(oldFile.exists()) {
					oldFile.delete();
				}
				newProfile = file.getName();
			} else { // 이미지 파일이 아니면 
				if(file.exists()) {
					file.delete(); // 삭제
				}
				rttr.addFlashAttribute("msgType", "실패 메세지");
				rttr.addFlashAttribute("msg", "이미지 파일만 업로드 가능합니다.");
				return "redirect:/memImageForm.do";
			}
		}
		// 새로운 이미지를 테이블에 업데이트 
		Member mvo = new Member();
		mvo.setMemID(memID);
		mvo.setMemProfile(newProfile);
		memberMapper.memProfileUpdate(mvo); // 이미지 업데이트 성공
		Member m = memberMapper.getMember(memID);
		// 세션을 새롭게 생성한다. (그전 세션은 프로필 적용이 안되어있으니까 프로필 적용된 세션을 만들어준다)
		session.setAttribute("mvo", m);
		rttr.addFlashAttribute("msgType", "성공 메세지");
		rttr.addFlashAttribute("msg", "이미지 변경이 성공했습니다.");
		return "redirect:/";
	}
}
