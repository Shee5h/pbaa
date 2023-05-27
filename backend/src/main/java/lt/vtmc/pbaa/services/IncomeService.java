package lt.vtmc.pbaa.services;

import lt.vtmc.pbaa.models.Income;
import lt.vtmc.pbaa.models.User;
import lt.vtmc.pbaa.payload.requests.IncomeInsertRequest;
import lt.vtmc.pbaa.payload.requests.IncomeUpdateRequest;
import lt.vtmc.pbaa.payload.responses.IncomeResponse;
import lt.vtmc.pbaa.repositories.IncomeRepository;
import lt.vtmc.pbaa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class IncomeService {
	
    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    @Autowired
    public IncomeService(IncomeRepository incomeRepository, UserRepository userRepository) {
        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
    }

    public IncomeResponse saveIncome(IncomeInsertRequest incomeRequest) {
        String currentPrincipalEmail = getCurrentPrincipalEmail();
    	User user = userRepository.findByEmail(currentPrincipalEmail).orElse(null);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String incomeName = incomeRequest.getIncomeName();
        Income income = new Income(
                user,
                incomeName.substring(0, 1).toUpperCase() + incomeName.substring(1),
                LocalDate.parse(incomeRequest.getDate(), formatter),
                BigDecimal.valueOf(Double.parseDouble(incomeRequest.getAmount())));
        incomeRepository.save(income);
        return new IncomeResponse(
                income.getId().toString(),
                incomeRequest.getIncomeName(),
                incomeRequest.getDate(),
                incomeRequest.getAmount());
    }

    public IncomeResponse updateIncome(IncomeUpdateRequest incomeUpdateRequest) {
        String currentPrincipalEmail = getCurrentPrincipalEmail();
        Optional<User> user = userRepository.findByEmail(currentPrincipalEmail);
        if (user.isEmpty()) {
            throw new RuntimeException("User does not exist");
        }
        List<Income> userIncomes = getAllIncomeByUser(user.get().getId());
        Income updatingIncome = incomeRepository.getById(Long.valueOf(incomeUpdateRequest.getIncomeId()));
        if (!userIncomes.contains(updatingIncome)) {
            throw new RuntimeException("User has not this income");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String incomeName = incomeUpdateRequest.getIncomeName();
        String updatingIncomeName = incomeName.substring(0, 1).toUpperCase() + incomeName.substring(1);
        updatingIncome.setIncomeName(updatingIncomeName);
        updatingIncome.setDate(LocalDate.parse(incomeUpdateRequest.getDate(), formatter));
        updatingIncome.setAmount(BigDecimal.valueOf(Double.parseDouble(incomeUpdateRequest.getAmount())));
        incomeRepository.save(updatingIncome);
        return new IncomeResponse(
                updatingIncome.getId().toString(),
                updatingIncomeName,
                incomeUpdateRequest.getDate(),
                incomeUpdateRequest.getAmount());
    }

    public IncomeResponse deleteIncome(String id) {
        String currentPrincipalEmail = getCurrentPrincipalEmail();
        Optional<User> user = userRepository.findByEmail(currentPrincipalEmail);
        if (user.isEmpty()) {
            throw new RuntimeException("User does not exist");
        }
        List<Income> userIncomes = getAllIncomeByUser(user.get().getId());
        Income deletingIncome = incomeRepository.getById(Long.valueOf(id));
        if (!userIncomes.contains(deletingIncome)) {
            throw new RuntimeException("User has not this income");
        }
        incomeRepository.delete(deletingIncome);
        return null;
    }

    private String getCurrentPrincipalEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public List<Income> getAllIncomeByUser(Long id) {
    	Optional<User> user = userRepository.findById(id);
    	return incomeRepository.findByUser(user);
    }
    
//    public List<Income> findByUserAndSort(String field){
//    	String currentPrincipalEmail = getCurrentPrincipalEmail();
//    	User user1 = userRepository.findByEmail(currentPrincipalEmail).orElse(null);
//    	Optional<User> user = Optional.of(user1);
//    	
//		return incomeRepository.findByUser(user, Sort.by(Sort.Direction.ASC, field));
//	}
    
    public Page<Income> getIncomeWithPagination(int offset, int pageSize){
    	String currentPrincipalEmail = getCurrentPrincipalEmail();
    	User user1 = userRepository.findByEmail(currentPrincipalEmail).orElse(null);
    	Optional<User> user = Optional.of(user1);
    	//Sort.Order order = new Sort.Order(Direction.DESC, "date");
    	
    	List<Income> income = incomeRepository.findByUser(user);
    	 Collections.sort(income, new Comparator() {

    	        public int compare(Object o1, Object o2) {
    	        	//comapre by date and then id in descending order
    	        	LocalDate x1 = ((Income) o1).getDate();
    	        	LocalDate x2 = ((Income) o2).getDate();
    	            int sComp = x2.compareTo(x1);

    	            if (sComp != 0) {
    	               return sComp;
    	            } 

    	            Long y1 = ((Income) o1).getId();
    	            Long y2 = ((Income) o2).getId();
    	            return y2.compareTo(y1);
    	    }});
    	 Pageable pageable = PageRequest.of(offset, pageSize);
    	 final int startp = (int)pageable.getOffset();
     	final int endp = Math.min((startp + pageable.getPageSize()), income.size());
     	final Page<Income> pagep = new PageImpl<>(income.subList(startp, endp), pageable, income.size());
    	 
    	//Page<Income> page = incomeRepository.findByUser(user, PageRequest.of(offset, pageSize, Sort.by("date").descending()));
    	
    	return pagep;
    	
    }
    public List<Income> findByUserByDate(String date){
    	String currentPrincipalEmail = getCurrentPrincipalEmail();
    	User user1 = userRepository.findByEmail(currentPrincipalEmail).orElse(null);
    	Optional<User> user = Optional.of(user1);
    	LocalDate lDate = LocalDate.parse(date);
    	
    	List<Income> list = incomeRepository.findByUser(user);
    	List<Income> sorted = new ArrayList<Income>();
    	
    	for (int i = 0; i < list.size(); i++) {
    		if(YearMonth.from(list.get(i).getDate()).equals(YearMonth.from(lDate))) {
    			sorted.add(list.get(i));
    		}
		}
		return sorted;
	}
//    public Page<Income> findByUserAndDate(String date, int offset, int pageSize){
//    	String currentPrincipalEmail = getCurrentPrincipalEmail();
//    	User user1 = userRepository.findByEmail(currentPrincipalEmail).orElse(null);
//    	Optional<User> user = Optional.of(user1);
//    	
//    	LocalDate initial = LocalDate.parse(date);
//    	LocalDate start = initial.withDayOfMonth(1);
//    	LocalDate end = initial.withDayOfMonth(initial.getMonth().length(initial.isLeapYear()));
//    	
//    	LocalDate lDate = LocalDate.parse(date);
//    	Pageable pageable = PageRequest.of(offset, pageSize);
////    	List<Income> list = incomeRepository.findByUserAndDateBetween(user, start, end, pageable);
////    	List<Income> sorted = new ArrayList<Income>();
//    	List<Income> list = incomeRepository.findByUserAndDateBetween(user, start, end);
//    	
//    	final int startp = (int)pageable.getOffset();
//    	final int endp = Math.min((startp + pageable.getPageSize()), list.size());
//    	final Page<Income> pagep = new PageImpl<>(list.subList(startp, endp), pageable, list.size());
//    	
////    	for (int i = 0; i < list.size(); i++) {
////    		if(YearMonth.from(list.get(i).getDate()).equals(YearMonth.from(lDate))) {
////    			sorted.add(list.get(i));
////    		}
////		}
//    	
//		return pagep;
//	}
    
    
    public Page<Income> findByUserAndDate(String date, int offset, int pageSize){
    	String currentPrincipalEmail = getCurrentPrincipalEmail();
    	User user1 = userRepository.findByEmail(currentPrincipalEmail).orElse(null);
    	Optional<User> user = Optional.of(user1);
    	
    	LocalDate initial = LocalDate.parse(date);
    	LocalDate start = initial.withDayOfMonth(1);
    	LocalDate end = initial.withDayOfMonth(initial.getMonth().length(initial.isLeapYear()));
    	
    	Page<Income> page = incomeRepository.findByUserAndDateBetween(user, start, end, PageRequest.of(offset, pageSize));

    	
		return page;
	}
}
