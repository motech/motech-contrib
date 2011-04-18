/**
 * MOTECH PLATFORM OPENSOURCE LICENSE AGREEMENT
 *
 * Copyright (c) 2011 Grameen Foundation USA.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of Grameen Foundation USA, nor its respective contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY GRAMEEN FOUNDATION USA AND ITS CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL GRAMEEN FOUNDATION USA OR ITS CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 */
package org.motechproject.outbox.dao;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.outbox.model.OutboundVoiceMessage;
import org.motechproject.outbox.model.OutboundVoiceMessageStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author yyonkov
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/persistenceOutboxCommon.xml"})
public class OutboundVoiceMessageDaoIT {
	@Autowired
	private OutboundVoiceMessageDao outboundVoiceMessageDao;
	private String partyId1 = "0001";
	private String partyId2 = "0002";

	@Before
	public void setUp() {
		Date now = DateUtils.truncate(new Date(), Calendar.DATE);
		for(int i = 0; i<20; i++) {
			OutboundVoiceMessage msg = new OutboundVoiceMessage();
			msg.setPartyId(i<10?partyId1:partyId2);
			msg.setCreationTime(now);
			msg.setExpirationDate(DateUtils.addDays(now, 1-2*(i&1)));
			msg.setStatus((i&2)>0?OutboundVoiceMessageStatus.PENDING:OutboundVoiceMessageStatus.PLAYED);
			outboundVoiceMessageDao.add(msg);
		}
		
	}
	@After
	public void tearDown() {
		for(OutboundVoiceMessage msg : outboundVoiceMessageDao.getAll()) {
			outboundVoiceMessageDao.remove(msg);
		}
	}
	@Test
	public void testGetNextPendingMessage() {
		OutboundVoiceMessage msg = outboundVoiceMessageDao.getNextPendingMessage(partyId1);
//		assertNotNull(msg);
	}
}
