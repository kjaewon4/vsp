      -- StartupItem 더미 데이터 20개 생성 (user_id = 4)
      -- ItemStatus: DRAFT, PENDING, APPROVED, REJECTED, ARCHIVED, DELETED
      -- CategoryType: IT_SOFTWARE, ECOMMERCE, EDUCATION, HEALTHCARE, MEDIA_CONTENT, FINTECH, SOCIAL_IMPACT, FOOD, FASHION_BEAUTY, LIFESTYLE, ETC

      INSERT INTO startup_items (title, description, status, category, views, user_id, created_at, updated_at) VALUES
      ('AI 기반 개인 맞춤형 학습 앱', '사용자의 학습 패턴을 분석하여 최적화된 커리큘럼을 제공하는 AI 교육 앱입니다.', 'APPROVED',
      'EDUCATION', 345, 4, NOW(), NOW()),
      ('친환경 재활용 의류 쇼핑몰', '버려지는 의류를 재활용하여 새로운 디자인의 옷을 판매하는 온라인 쇼핑몰입니다.', 'APPROVED',
      'ECOMMERCE', 210, 4, NOW(), NOW()),
      ('블록체인 기반 부동산 투자 플랫폼', '소액으로도 부동산에 투자할 수 있는 블록체인 기반의 안전한 투자 플랫폼입니다.', 'APPROVED',
      'FINTECH', 520, 4, NOW(), NOW()),
      ('명상 및 심리 상담 모바일 앱', '스트레스 해소와 정신 건강 증진을 위한 명상 가이드 및 비대면 심리 상담 서비스입니다.',
      'APPROVED', 'HEALTHCARE', 180, 4, NOW(), NOW()),
     ('인터랙티브 웹툰 제작 도구', '코딩 없이도 사용자가 직접 스토리를 만들고 인터랙티브 요소를 추가할 수 있는 웹툰 제작 툴입니다.',
      'APPROVED', 'MEDIA_CONTENT', 290, 4, NOW(), NOW()),
     ('지역 농산물 직거래 플랫폼', '소비자가 생산자로부터 신선한 농산물을 직접 구매할 수 있도록 연결하는 플랫폼입니다.', 'APPROVED',
      'FOOD', 155, 4, NOW(), NOW()),
     ('스마트 미러 피트니스 솔루션', '거울을 통해 운동 자세를 교정해주고 개인 트레이닝을 제공하는 스마트 미러입니다.', 'APPROVED',
      'LIFESTYLE', 400, 4, NOW(), NOW()),
     ('AI 기반 법률 자문 서비스', '복잡한 법률 문제를 AI가 분석하여 맞춤형 자문을 제공하는 온라인 서비스입니다.', 'APPROVED',
      'IT_SOFTWARE', 380, 4, NOW(), NOW()),
     ('업사이클링 가구 디자인 스튜디오', '버려진 가구를 예술 작품으로 재탄생시키는 업사이클링 가구 전문 스튜디오입니다.', 'APPROVED',
      'LIFESTYLE', 120, 4, NOW(), NOW()),
     ('가상현실(VR) 기반 외국어 학습', '실제와 같은 환경에서 원어민과 대화하며 외국어를 배울 수 있는 VR 학습 프로그램입니다.',
      'APPROVED', 'EDUCATION', 270, 4, NOW(), NOW()),
     ('개인 맞춤형 영양제 추천 서비스', '유전자 검사 및 생활 습관 분석을 통해 개인에게 최적화된 영양제를 추천하고 정기 배송합니다.',
      'APPROVED', 'HEALTHCARE', 310, 4, NOW(), NOW()),
     ('크라우드 펀딩 기반 예술 프로젝트', '신진 예술가들이 작품 활동을 위한 자금을 모으고 대중과 소통하는 크라우드 펀딩
      플랫폼입니다.', 'APPROVED', 'SOCIAL_IMPACT', 190, 4, NOW(), NOW()),
     ('반려동물 맞춤형 식단 배송', '반려동물의 건강 상태와 품종에 맞춰 영양학적으로 설계된 식단을 정기적으로 배송합니다.', 'APPROVED'
      , 'FOOD', 230, 4, NOW(), NOW()),
     ('AI 패션 스타일리스트 앱', '사용자의 체형, 취향, TPO에 맞춰 AI가 의상을 추천하고 코디를 제안하는 앱입니다.', 'APPROVED',
      'FASHION_BEAUTY', 450, 4, NOW(), NOW()),
     ('온라인 코딩 교육 플랫폼 (게임화)', '게임처럼 재미있게 코딩을 배울 수 있도록 설계된 온라인 교육 플랫폼입니다.', 'APPROVED',
      'EDUCATION', 360, 4, NOW(), NOW()),
     ('스마트팜 솔루션', 'IoT 기술을 활용하여 농작물 재배 환경을 최적화하고 생산성을 높이는 스마트팜 솔루션입니다.', 'APPROVED',
      'IT_SOFTWARE', 200, 4, NOW(), NOW()),
     ('폐기물 제로 라이프스타일 샵', '일회용품 사용을 줄이고 지속 가능한 소비를 지향하는 친환경 제품 전문 온라인 샵입니다.',
      'APPROVED', 'ECOMMERCE', 170, 4, NOW(), NOW()),
     ('인공지능 기반 뉴스 큐레이션', '사용자의 관심사에 맞춰 전 세계 뉴스를 실시간으로 분석하고 요약하여 제공하는 AI 뉴스
      서비스입니다.', 'APPROVED', 'MEDIA_CONTENT', 300, 4, NOW(), NOW()),
     ('가상 피팅룸 서비스', '온라인 쇼핑 시 옷을 직접 입어보지 않고도 가상으로 피팅해볼 수 있는 서비스입니다.', 'APPROVED',
      'FASHION_BEAUTY', 280, 4, NOW(), NOW()),
     ('시니어 맞춤형 돌봄 로봇', '독거노인을 위한 정서적 교감 및 건강 모니터링 기능을 제공하는 AI 돌봄 로봇입니다.', 'APPROVED',
      'SOCIAL_IMPACT', 160, 4, NOW(), NOW());

